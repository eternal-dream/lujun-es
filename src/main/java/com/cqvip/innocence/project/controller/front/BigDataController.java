package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmArticleVisitLog;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.entity.ArticleDoc;
import com.cqvip.innocence.project.model.enums.BusinessType;
import com.cqvip.innocence.project.model.enums.SearchType;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 大数据
 * @Author eternal
 * @Date 2021/10/11
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/bigData")
public class BigDataController extends AbstractController {

 @Autowired
 private ArmUserInfoService userInfoService;

 @Autowired
 private ArmOperLogService operLogService;

 @Autowired
 private ArmArticleVisitLogService articleVisitLogService;

 @Autowired
 private ArmArticleDownLogService armArticleDownLogService;

 @Autowired
 private ArmFavouriteService favouriteService;

 @Autowired
 private DocumentService documentService;

 /**
  * 获取读者人数(注册用户)
  * @return
  */
 @GetMapping("getReaderCount")
 @Log(title = "访问大数据页面")
 public JsonResult getReaderCount(){
  LambdaQueryWrapper<ArmUserInfo> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(ArmUserInfo::getReaderSrc, SearchType.ReaderSrc.CampusUser);
  int countCampusUser = userInfoService.count(wrapper);
  LambdaQueryWrapper<ArmUserInfo> wrapper2 = new LambdaQueryWrapper<>();
  wrapper2.eq(ArmUserInfo::getReaderSrc, SearchType.ReaderSrc.OffCampusUser);
  int countOffCampusUser = userInfoService.count(wrapper2);
  return JsonResult.Get().put("countCampusUser",countCampusUser).put("countOffCampusUser",countOffCampusUser);
 }

 @GetMapping("getVisitCount")
 public JsonResult getVisitCount(visitCountType type) throws ParseException {
  Map visitCount = new HashMap();
  switch (type){
   case year:
    visitCount.put("trendData",operLogService.getVisitCountOfYear());
    break;
   case month:
    visitCount.put("trendData",operLogService.getVisitCountOfMonth());
    break;
   case week:
    visitCount.put("trendData",operLogService.getVisitCountOfWeek());
    break;
   case day:
    visitCount.put("trendData",operLogService.getVisitCountOfDay());
    break;
  }
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  LocalTime time = LocalTime.of(0, 0, 1);
  int yearCount = operLogService.countVisitTimes(sdf.parse(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()),time).toString()));
  int monthCount = operLogService.countVisitTimes(sdf.parse(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),time).toString()));
  int weekCount = operLogService.countVisitTimes(sdf.parse(LocalDateTime.of(LocalDate.now().with(DayOfWeek.MONDAY),time).toString()));
  int dayCount = operLogService.countVisitTimes(sdf.parse(LocalDateTime.of(LocalDate.now(),time).toString()));
  int count = operLogService.countVisitTimes(null);
  visitCount.put("yearCount",yearCount);
  visitCount.put("monthCount",monthCount);
  visitCount.put("weekCount",weekCount);
  visitCount.put("dayCount",dayCount);
  visitCount.put("total",count);

  return JsonResult.Get("visitCount",visitCount);
 }

 /**
  *数字资源利用（下载/访问/收藏）
  */
 @GetMapping("digitalResourceUtilization")
 public JsonResult digitalResourceUtilization(Date beginTime,Date endTime,String type) throws ParseException {
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  if(beginTime == null){
   beginTime = sdf.parse(LocalDateTime.now().plusYears(-1).toString());
   type = "month";
  }
  if(endTime == null){
   endTime = new Date();
  }
  List<Map> visitTrendData = articleVisitLogService.statVisitTrend(beginTime,endTime,type);
  List<Map> downLoadTrendData = armArticleDownLogService.statDownLoadTrend(beginTime,endTime,type);
  List<Map> collectTrendData = favouriteService.statCollectTrend(beginTime,endTime,type);
  return JsonResult.Get()
   .put("visitTrendData",visitTrendData)
   .put("downLoadTrendData",downLoadTrendData)
   .put("collectTrendData",collectTrendData);
 }

// @GetMapping("statBookNumberByClc")
// public JsonResult statBookNumberByClc() throws IOException {
//  SearchSourceBuilder builder = new SearchSourceBuilder();
//  TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("clcNoFacet").field("clc_no");
//  builder.aggregation(aggregationBuilder);
//  Map facet = documentService.getFacetByQuery(builder, ArticleDoc.class);
//  return JsonResult.Get();
// }

 private enum visitCountType{
  year,//当年各月
  month,//当月各日
  week,//本周各日
  day,//当天每小时
 }
}