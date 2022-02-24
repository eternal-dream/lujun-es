package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmArticleSearchLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源检索日志 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-09-15
 */
public interface ArmArticleSearchLogMapper extends BaseMapper<ArmArticleSearchLog> {

 List<Map> getRecords(@Param("id") Long id, @Param("tableName") String tableName, @Param("current") long current, @Param("size") long size, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

 List<Map> getAllTypeRecords(@Param("id") Long id, @Param("current") long current, @Param("size") long size,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

 Integer countRecords(@Param("id") Long id,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

 Integer countRecordsByTableName(@Param("id") Long id,@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("tableName") String tableName);
}
