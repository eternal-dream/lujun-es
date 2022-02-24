package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cqvip.innocence.project.model.dto.MenuNode;
import com.cqvip.innocence.project.model.dto.ResourceNode;
import com.cqvip.innocence.project.model.entity.ArmResource;
import com.cqvip.innocence.project.mapper.ArmResourceMapper;
import com.cqvip.innocence.project.model.entity.ArmRole;
import com.cqvip.innocence.project.service.ArmResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Service
public class ArmResourceServiceImpl extends ServiceImpl<ArmResourceMapper, ArmResource> implements ArmResourceService {

    @Override
    public List<ArmResource> getRoleResources(ArmRole role) {
        return baseMapper.getRoleResources(role);
    }

    @Override
    public List<ResourceNode> getResourceTreeData() {
        List<ResourceNode> superResources = baseMapper.getSuperResources();
        List<ResourceNode> notsuperResources = baseMapper.getNotSuperResources();
        if (ObjectUtil.isNotNull(notsuperResources)) {
            Set<Long> set = new HashSet<>(notsuperResources.size());
            superResources.forEach(item -> getChild(item, notsuperResources, 0, set));
            return superResources;
        }

        return new ArrayList<>();
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
    private void getChild(ResourceNode menuNode, List<ResourceNode> menuNodeList, Integer level, Set<Long> set) {
        //过滤深度
        if (level == 0) {
            List<ResourceNode> childList = new ArrayList<>();
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
