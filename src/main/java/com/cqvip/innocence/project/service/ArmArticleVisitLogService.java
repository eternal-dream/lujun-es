package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmArticleVisitLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源浏览日志 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-15
 */
public interface ArmArticleVisitLogService extends IService<ArmArticleVisitLog> {

 /**
  * 按年或月查询访问趋势
  * @param beginTime
  * @param endTime
  * @param type
  * @return
  */
 List<Map> statVisitTrend(Date beginTime, Date endTime, String type);
}
