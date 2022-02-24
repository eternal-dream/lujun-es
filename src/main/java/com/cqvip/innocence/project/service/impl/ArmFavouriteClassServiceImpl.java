package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.project.mapper.ArmFavouriteMapper;
import com.cqvip.innocence.project.model.entity.ArmFavourite;
import com.cqvip.innocence.project.model.entity.ArmFavouriteClass;
import com.cqvip.innocence.project.mapper.ArmFavouriteClassMapper;
import com.cqvip.innocence.project.service.ArmFavouriteClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.service.ArmFavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户收藏分类表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
@Service
public class ArmFavouriteClassServiceImpl extends ServiceImpl<ArmFavouriteClassMapper, ArmFavouriteClass> implements ArmFavouriteClassService {

    @Autowired
    private ArmFavouriteMapper favouriteMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long userId, Long id) {
        LambdaQueryWrapper<ArmFavourite> eq = new QueryWrapper<ArmFavourite>().lambda()
                .eq(ArmFavourite::getClassId, id).eq(ArmFavourite::getUserId, userId);
        favouriteMapper.delete(eq);
        baseMapper.deleteById(id);
    }
}
