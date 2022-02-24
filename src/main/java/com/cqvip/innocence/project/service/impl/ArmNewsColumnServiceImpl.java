package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.ArmNewsColumnMapper;
import com.cqvip.innocence.project.model.entity.ArmNewsColumn;
import com.cqvip.innocence.project.service.ArmNewsColumnService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 新闻栏目表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Service
public class ArmNewsColumnServiceImpl extends ServiceImpl<ArmNewsColumnMapper, ArmNewsColumn> implements ArmNewsColumnService {

    @Override
    public List<ArmNewsColumn> getNewsColumnsOfTreeData(Integer level) {
        //非顶级菜单
        List<ArmNewsColumn> notSuperMenuList = baseMapper.getAllNotSuperNewsColumn();
        //顶级菜单
        List<ArmNewsColumn> superMenuList = baseMapper.getAllSuperNewsColumn();

        if (ObjectUtil.isNotNull(notSuperMenuList)) {
            Set<Long> set = new HashSet<>(notSuperMenuList.size());
            superMenuList.forEach(item -> getChild(item, notSuperMenuList, level, set));
            return superMenuList;
        }

        return new ArrayList<>();
    }

    /**
     * 递归获取当前节点的子节点，并组装
     *
     * @param node     节点
     * @param nodeList 子节点列表
     * @param level        深度
     * @param set          已递归的节点id
     * @author 01
     * @date 2021/8/25 16:24
     */
    private void getChild(ArmNewsColumn node, List<ArmNewsColumn> nodeList, Integer level, Set<Long> set) {
        //过滤深度
        if (level == 0) {
            List<ArmNewsColumn> childList = new ArrayList<>();
            nodeList.stream()
                    //过滤已经递归了的节点
                    .filter(item -> !set.contains(item.getId()))
                    //判断是否是父子关系
                    .filter(item -> NumberUtil.compare(item.getParentId(), node.getId()) == 0)
                    .filter(item -> set.size() <= nodeList.size())
                    .forEach(item -> {
                        //set保存已经递归了的id
                        set.add(item.getId());
                        //递归
                        getChild(item, nodeList, level, set);
                        //加入子节点集合
                        childList.add(item);
                    });
            node.setChildren(childList);
        }
    }
}
