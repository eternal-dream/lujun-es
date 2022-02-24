package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmNewsClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发布内容分类表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface ArmNewsClassMapper extends BaseMapper<ArmNewsClass> {

    List<ArmNewsClass> getSoftwareClassByColumnName(@Param("columnName") String columnName);

}
