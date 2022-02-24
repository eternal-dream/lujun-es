package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmFavouriteClass;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户收藏分类表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
public interface ArmFavouriteClassService extends IService<ArmFavouriteClass> {

    /**
     * 功能描述
     * @author Innocence
     * @date 2021/9/26
     * @param userId 用户id
     * @param id 收藏分类id
     * @return java.lang.Boolean
     */
    void deleteById(Long userId,Long id);

}
