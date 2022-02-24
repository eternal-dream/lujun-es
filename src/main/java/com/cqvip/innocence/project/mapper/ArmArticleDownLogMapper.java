package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmArticleDownLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源下载日志 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-09-27
 */
public interface ArmArticleDownLogMapper extends BaseMapper<ArmArticleDownLog> {

 List<Map> statDownLoadTrend(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("type") String type);
}
