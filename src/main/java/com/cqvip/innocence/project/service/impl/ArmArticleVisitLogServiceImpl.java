package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.ArmArticleVisitLog;
import com.cqvip.innocence.project.mapper.ArmArticleVisitLogMapper;
import com.cqvip.innocence.project.service.ArmArticleVisitLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源浏览日志 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-15
 */
@Service
public class ArmArticleVisitLogServiceImpl extends ServiceImpl<ArmArticleVisitLogMapper, ArmArticleVisitLog> implements ArmArticleVisitLogService {

 @Override
 public List<Map> statVisitTrend(Date beginTime, Date endTime, String type) {
  return fillData(baseMapper.statVisitTrend(beginTime,endTime,type),beginTime,endTime,type);
 }

 private List<Map> fillData(List<Map> trendData,Date begin,Date end,String type ) {
  DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy");
  DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyyMM");
  LocalDateTime beginTime = LocalDateTime.ofInstant(begin.toInstant(), ZoneId.systemDefault());
  LocalDateTime endTime = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
  while (!beginTime.isAfter(endTime)){
   boolean exists = false;
   Integer key = null;
   if(type.equals("year") ){
    key = Integer.parseInt(yearFormat.format(beginTime));
    beginTime = beginTime.plusYears(1);
   }else if(type.equals("month")){
    key = Integer.parseInt(monthFormat.format(beginTime));
    beginTime = beginTime.plusMonths(1);
   }
   for (Map map : trendData) {
    if((key+"").equals(map.get("DATE").toString())){
     exists = true;
     break;
    }
   }
   if(!exists){
    Map tempData = new HashMap();
    tempData.put("COUNT",0);
    tempData.put("DATE",key);
    trendData.add(tempData);
   }
  }
  trendData = trendData.stream().sorted(Comparator.comparingInt(v -> Integer.parseInt(v.get("DATE").toString())))
   .collect(Collectors.toList());
  return trendData;
 }
}
