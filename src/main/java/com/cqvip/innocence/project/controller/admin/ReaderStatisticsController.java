package com.cqvip.innocence.project.controller.admin;

import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.service.ArmUserInfoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 读者统计页面
 * @Author eternal
 * @Date 2021/8/23
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/readerStatistics")
public class ReaderStatisticsController extends AbstractController {

 @Autowired
 private ArmUserInfoLogService userInfoLogService;

 /**
  * 获取近7天/30天/90天/365天活跃用户数
  * @return
  */
 @GetMapping("getActiveUserNumber")
 public JsonResult getActiveUserNumber(){


  return JsonResult.Get();
 }

}
