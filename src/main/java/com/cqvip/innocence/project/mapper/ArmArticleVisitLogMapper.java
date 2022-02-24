package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmArticleVisitLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源浏览日志 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-09-15
 */
public interface ArmArticleVisitLogMapper extends BaseMapper<ArmArticleVisitLog> {

 List<Map> statVisitTrend(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("type") String type);
}
