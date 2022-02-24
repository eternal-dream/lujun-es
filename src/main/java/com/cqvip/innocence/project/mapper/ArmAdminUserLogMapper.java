package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmAdminUserLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 * 管理员操作日志 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
public interface ArmAdminUserLogMapper extends BaseMapper<ArmAdminUserLog> {

    /**
     * 根据id彻底删除管理员日志（物理删除）
     * @author Innocence
     * @date 2021/8/25
     * @param id
     * @return int
     */
    int deleteByIdToXml(@Param("id") Serializable id);

}
