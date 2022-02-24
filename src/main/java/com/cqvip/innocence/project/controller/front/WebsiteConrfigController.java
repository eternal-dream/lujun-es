package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmWebsiteConfig;
import com.cqvip.innocence.project.service.ArmWebsiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author eternal
 * @Date 2021/9/22
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/websiteConfig")
public class WebsiteConrfigController extends AbstractController {

 @Autowired
 private ArmWebsiteConfigService configService;

 @GetMapping("getLornDueDate")
 @NoLogin
 public JsonResult getLornDueDate(String day){
  ArmWebsiteConfig config = configService.list().get(0);
  Integer lendingDays = config.getLendingDays();
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
  LocalDate date = LocalDate.parse(day,dtf);
  LocalDate lornDueDate = date.plusDays(lendingDays);
  return JsonResult.Get("lornDueDate",lornDueDate);
 }
}