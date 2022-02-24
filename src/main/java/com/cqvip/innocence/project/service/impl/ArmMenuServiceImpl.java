package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.ArmMenuMapper;
import com.cqvip.innocence.project.model.dto.MenuNode;
import com.cqvip.innocence.project.model.entity.ArmMenu;
import com.cqvip.innocence.project.service.ArmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单管理表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-25
 */
@Service
public class ArmMenuServiceImpl extends ServiceImpl<ArmMenuMapper, ArmMenu> implements ArmMenuService {

    @Override
    public List<MenuNode> getMenuTreeDataByLevel(Integer level) {
        //非顶级菜单
        List<MenuNode> notSuperMenuList = baseMapper.getAllNotSuperMenu();
        //顶级菜单
        List<MenuNode> superMenuList = baseMapper.getAllSuperMenu();

        if (ObjectUtil.isNotNull(notSuperMenuList)) {
            Set<Long> set = new HashSet<>(notSuperMenuList.size());
            superMenuList.forEach(item -> getChild(item, notSuperMenuList, level, set));
            return superMenuList;
        }

        return new ArrayList<>();
    }

    @Override
    public List<MenuNode> getParentMenuByUrl(String url) {
        LambdaQueryWrapper<ArmMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmMenu::getUrl,url);

        List<ArmMenu> menuList = this.list(wrapper);
        if(menuList.size() == 0){
            return new ArrayList<>();
        }
        ArmMenu menu = menuList.get(0);
        Long parentId = menu.getParentId();
        if(parentId == null){
            parentId = menu.getId();
        }
        MenuNode menuNode = baseMapper.getMenuNodeById(parentId);
        List<MenuNode> menuNodeList = new ArrayList<>();
        menuNodeList.add(menuNode);
        List<MenuNode> notSuperMenuList = baseMapper.getAllNotSuperMenu();

        if (ObjectUtil.isNotNull(notSuperMenuList)) {
            Set<Long> set = new HashSet<>(notSuperMenuList.size());
            menuNodeList.forEach(item -> getChild(item, notSuperMenuList, 0, set));
        }
        return menuNodeList;
    }

    /**
     * 递归获取当前节点的子节点，并组装
     *
     * @param menuNode     节点
     * @param menuNodeList 子节点列表
     * @param level        深度
     * @param set          已递归的节点id
     * @author 01
     * @date 2021/8/25 16:24
     */
    private void getChild(MenuNode menuNode, List<MenuNode> menuNodeList, Integer level, Set<Long> set) {
        //过滤深度
        if (menuNode.getLevel() < level || level == 0) {
            List<MenuNode> childList = new ArrayList<>();
            menuNodeList.stream()
                    //过滤已经递归了的节点
                    .filter(item -> !set.contains(item.getId()))
                    //判断是否是父子关系
                    .filter(item -> NumberUtil.compare(item.getParentId(), menuNode.getId()) == 0)
                    .filter(item -> set.size() <= menuNodeList.size())
                    .forEach(item -> {
                        //set保存已经递归了的id
                        set.add(item.getId());
                        //递归
                        getChild(item, menuNodeList, level, set);
                        //加入子节点集合
                        childList.add(item);
                    });
            menuNode.setChildren(childList);
        }
    }
}
