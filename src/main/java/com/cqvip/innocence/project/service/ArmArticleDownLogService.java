package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmArticleDownLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源下载日志 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-27
 */
public interface ArmArticleDownLogService extends IService<ArmArticleDownLog> {

 /**
  * 按年/月查询下载趋势图
  * @param beginTime
  * @param endTime
  * @param type
  * @return
  */
 List<Map> statDownLoadTrend(Date beginTime, Date endTime, String type);
}
