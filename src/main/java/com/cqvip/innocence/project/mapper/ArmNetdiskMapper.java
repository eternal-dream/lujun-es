package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmNetdisk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户网盘表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmNetdiskMapper extends BaseMapper<ArmNetdisk> {

    /**
     * 管理员获取个人网盘分页列表mapper
     * @author Innocence
     * @date 2021/8/23
     * @param disk
     * @param page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cqvip.innocence.project.model.entity.ArmNetdisk>
     */
    Page<ArmNetdisk> getPageList(Page page,@Param("disk") ArmNetdisk disk);
}
