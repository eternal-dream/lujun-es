package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmNewsLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 * 新闻日志 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface ArmNewsLogMapper extends BaseMapper<ArmNewsLog> {

    /**
     * 根据id彻底删除新闻日志（物理删除）
     * @author Innocence
     * @date 2021/8/25
     * @param id
     * @return int
     */
    int deleteByIdToXml(@Param("id") Serializable id);
}
