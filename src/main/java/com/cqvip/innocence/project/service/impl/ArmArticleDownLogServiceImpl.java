package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.ArmArticleDownLog;
import com.cqvip.innocence.project.mapper.ArmArticleDownLogMapper;
import com.cqvip.innocence.project.service.ArmArticleDownLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源下载日志 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-27
 */
@Service
public class ArmArticleDownLogServiceImpl extends ServiceImpl<ArmArticleDownLogMapper, ArmArticleDownLog> implements ArmArticleDownLogService {

 @Override
 public List<Map> statDownLoadTrend(Date beginTime, Date endTime, String type) {
  return fillData(baseMapper.statDownLoadTrend(beginTime,endTime,type),beginTime,endTime,type);
 }

 private List<Map> fillData(List<Map> trendData,Date begin,Date end,String type ) {
  DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy");
  DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyyMM");
  LocalDateTime beginTime = LocalDateTime.ofInstant(begin.toInstant(), ZoneId.systemDefault());
  LocalDateTime endTime = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
  while (!beginTime.isAfter(endTime)){
   boolean exists = false;
   Integer key = null;
   if("year".equals(type)){
    key = Integer.parseInt(yearFormat.format(beginTime));
    beginTime = beginTime.plusYears(1);
   }else if("month".equals(type)){
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
