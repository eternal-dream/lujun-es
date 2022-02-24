package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.project.model.entity.ArmClcClassInfo;
import com.cqvip.innocence.project.mapper.ArmClcClassInfoMapper;
import com.cqvip.innocence.project.service.ArmClcClassInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 中图分类表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-08
 */
@Service
public class ArmClcClassInfoServiceImpl extends ServiceImpl<ArmClcClassInfoMapper, ArmClcClassInfo> implements ArmClcClassInfoService {

    @Override
    public List<ArmClcClassInfo> getTreeData() {
        LambdaQueryWrapper<ArmClcClassInfo> parentQuery = new QueryWrapper<ArmClcClassInfo>().lambda()
                .eq(ArmClcClassInfo::getDomainType, 1).eq(ArmClcClassInfo::getParentId, 0L);
        List<ArmClcClassInfo> parentList = baseMapper.selectList(parentQuery);
        LambdaQueryWrapper<ArmClcClassInfo> childQuery = new QueryWrapper<ArmClcClassInfo>().lambda()
                .eq(ArmClcClassInfo::getDomainType, 1).ne(ArmClcClassInfo::getParentId, 0L);
        List<ArmClcClassInfo> childList = baseMapper.selectList(childQuery);
        Set<Long> set = new HashSet<>(childList.size());
        parentList.forEach(item -> getChild(item, childList, set));
        return parentList;

    }

    private void getChild(ArmClcClassInfo classNode, List<ArmClcClassInfo> classNodeList, Set<Long> set) {
        //过滤深度
        List<ArmClcClassInfo> childList = new ArrayList<>();
        classNodeList.stream()
                //过滤已经递归了的节点
                .filter(item -> !set.contains(item.getId()))
                //判断是否是父子关系
                .filter(item -> NumberUtil.compare(item.getParentId(), classNode.getDomainId()) == 0)
                .filter(item -> set.size() <= classNodeList.size())
                .forEach(item -> {
                    //set保存已经递归了的id
                    set.add(item.getId());
                    //递归
                    getChild(item, classNodeList, set);
                    //加入子节点集合
                    childList.add(item);
                });
        classNode.setChildList(childList);
    }
}
