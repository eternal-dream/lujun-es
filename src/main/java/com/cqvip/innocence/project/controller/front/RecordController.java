package com.cqvip.innocence.project.controller.front;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.service.ArmArticleDownLogService;
import com.cqvip.innocence.project.service.ArmArticleSearchLogService;
import com.cqvip.innocence.project.service.ArmArticleVisitLogService;
import com.cqvip.innocence.project.service.ArmFavouriteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 数字资源应用(用户浏览记录日志)
 * @Author eternal
 * @Date 2021/9/27
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/records")
public class RecordController extends AbstractController {

 @Autowired
 private ArmArticleSearchLogService searchLogService;

 @Autowired
 private ArmArticleVisitLogService visitLogService;

 @Autowired
 private ArmArticleDownLogService downLogService;

 @Autowired
 private ArmFavouriteService favouriteService;

 @Autowired
 private RedisTemplate redisTemplate;

 /**
  * 统计当前登录用户各项操作记录的数量
  * @return
  */
 @GetMapping("countRecords")
 public JsonResult countRecords(HttpServletRequest request){
  String sessionId = request.getSession().getId();
  ArmUserInfo info = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
  LambdaQueryWrapper<ArmArticleSearchLog> searchLogWrapper = new LambdaQueryWrapper<>();
  LambdaQueryWrapper<ArmArticleVisitLog> visitLogWrapper = new LambdaQueryWrapper<>();
  LambdaQueryWrapper<ArmArticleDownLog> downLogWrapper = new LambdaQueryWrapper<>();
  LambdaQueryWrapper<ArmFavourite> favouriteWrapper = new LambdaQueryWrapper<>();
  searchLogWrapper.eq(ArmArticleSearchLog::getUserId,info.getId());
  visitLogWrapper.eq(ArmArticleVisitLog::getUserId,info.getId());
  downLogWrapper.eq(ArmArticleDownLog::getUserId,info.getId());
  favouriteWrapper.eq(ArmFavourite::getUserId,info.getId());
  int countSearchLog = searchLogService.count(searchLogWrapper);
  int countVisitLog = visitLogService.count(visitLogWrapper);
  int countDownLog = downLogService.count(downLogWrapper);
  int countFavourite = favouriteService.count(favouriteWrapper);
  Map<String,Integer> recordsNumber = new HashMap<>();
  recordsNumber.put("浏览",countVisitLog);
  recordsNumber.put("下载",countDownLog);
  recordsNumber.put("收藏",countFavourite);
  recordsNumber.put("检索",countSearchLog);
  recordsNumber.put("全部",(countSearchLog+countDownLog+countFavourite+countVisitLog));
  return JsonResult.Get("recordsNumber",recordsNumber);
 }

 @GetMapping("getRecords")
 @Log(title = "数字资源应用查询")
 public JsonResult getRecords(String type, HttpServletRequest request, Date startTime,Date endTime){
  List<Map> records = searchRecords(type,request,startTime,endTime,getPageParams());
  Integer count = countRecords(type,request,startTime,endTime);
  return JsonResult.Get("records", JSON.toJSON(records)).put("total",count);
 }

 @GetMapping("exportRecords")
 public void exportRecords(String type, HttpServletRequest request, Date startTime, Date endTime, HttpServletResponse response) throws UnsupportedEncodingException {
  List<Map> records = searchRecords(type,request,startTime,endTime,new Page(1,1000));
  List<Map> exportDataList = new ArrayList<>();
  records.forEach(item->{
   Map dataMap = new HashMap();
   if(item.get("DISPLAY_INFO")!=null){
    dataMap.put("title",item.get("DISPLAY_INFO"));
   }else{
    dataMap.put("title",item.get("TITLE"));
   }
   if(item.get("TYPE") == null){
    dataMap.put("type",type);
   }else{
    dataMap.put("type",item.get("TYPE"));
   }
   dataMap.put("createTime",item.get("CREATE_TIME"));
   exportDataList.add(dataMap);
  });
  ExcelWriter writer = ExcelUtil.getWriter();
  writer.addHeaderAlias("title", "标题");
  writer.addHeaderAlias("createTime", "时间");
  writer.addHeaderAlias("type", "类型");
  writer.write(exportDataList);
  response.setContentType("application/vnd.ms-excel;charset=utf-8");
  response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
  String fileName = URLEncoder.encode("数字资源应用", "UTF-8");
  response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
  ServletOutputStream out = null;

  try {
   out = response.getOutputStream();
   writer.flush(out, true);
  } catch (IOException e) {
   e.printStackTrace();
  } finally {
   writer.close();
  }
  IoUtil.close(out);
 }

 private Integer countRecords(String type, HttpServletRequest request, Date startTime, Date endTime){
  String sessionId = request.getSession().getId();
  ArmUserInfo info = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
  Integer count = 0;
  String tableName = getTableName(type);

  if(endTime != null){
   Calendar c = Calendar.getInstance();
   c.setTime(endTime);
   c.add(Calendar.DAY_OF_MONTH, 1);
   Date tomorrow = c.getTime();//这是明天
   endTime = tomorrow;
  }

  if(StringUtils.isBlank(tableName)){//全部
   count = searchLogService.countRecords(info.getId(),startTime,endTime);
  }else{
   count = searchLogService.countRecords(info.getId(),tableName,startTime,endTime);
  }
  return count;
 }

 private List<Map> searchRecords(String type, HttpServletRequest request, Date startTime, Date endTime, Page page){
  String sessionId = request.getSession().getId();
  ArmUserInfo info = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
  List<Map> records = null;
  String tableName = getTableName(type);

  if(endTime != null){
   Calendar c = Calendar.getInstance();
   c.setTime(endTime);
   c.add(Calendar.DAY_OF_MONTH, 1);
   Date tomorrow = c.getTime();//这是明天
   endTime = tomorrow;
  }
  if(StringUtils.isBlank(tableName)){//全部
   records = searchLogService.getRecords(info.getId(),startTime,endTime,page);
  }else{
   records = searchLogService.getRecords(info.getId(),tableName,startTime,endTime,page);
  }
  return records;
 }

 private String getTableName(String type) {
  String tableName = null;
  switch (type){
   case "浏览":
    tableName = "ARM_ARTICLE_VISIT_LOG";
    break;
   case "下载":
    tableName = "ARM_ARTICLE_DOWN_LOG";
    break;
   case "收藏":
    tableName = "ARM_FAVOURITE";
    break;
   case "检索":
    tableName = "ARM_ARTICLE_SEARCH_LOG";
    break;
   default:
    break;
  }
  return tableName;
 }

}