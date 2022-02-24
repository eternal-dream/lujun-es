package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmLoginLog;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.service.ArmLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author eternal
 * @Date 2021/10/21
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/loginLog")
public class LoginLogController extends AbstractController {

 @Autowired
 private ArmLoginLogService loginLogService;

 /**
  * 登录页获取访问次数
  * @return
  */
 @NoLogin
 @GetMapping("getVisitTimes")
 public JsonResult getVisitTimes() throws ParseException {
  SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
  LambdaQueryWrapper<ArmLoginLog> nowWrapper = new LambdaQueryWrapper<>();
  nowWrapper.ge(BaseModel::getCreateTime, timeFormat.parse(LocalDateTime.now().plusHours(-1).toString()));
  int countNow = loginLogService.count(nowWrapper);

  LambdaQueryWrapper<ArmLoginLog> todayWrapper = new LambdaQueryWrapper<>();
  todayWrapper.ge(BaseModel::getCreateTime, dayFormat.parse(LocalDate.now().toString()));
  int countToday = loginLogService.count(todayWrapper);

  int count = loginLogService.count();
  return JsonResult.Get()
   .put("countNow",countNow)
   .put("countToday",countToday)
   .put("count",count);
 }
}