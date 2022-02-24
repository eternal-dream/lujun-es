package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员操作日志 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
public interface ArmOperLogService extends IService<ArmOperLog> {

 /**
  * 获取当年各月访问量
  * @return
  */
 List<Map> getVisitCountOfYear();

 /**
  * 根据时间范围获取访问次数
  * @return
  */
 int countVisitTimes(Date limit);

 /**
  * 当月每天的访问量
  * @return
  */
 List<Map> getVisitCountOfMonth();

 /**
  * 本周每天访问量
  * @return
  */
 List<Map> getVisitCountOfWeek() throws ParseException;

 /**
  * 今天各时访问量
  * @return
  */
 List<Map> getVisitCountOfDay();

}
