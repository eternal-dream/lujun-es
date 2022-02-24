package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmArticleSearchLog;
import com.cqvip.innocence.project.mapper.ArmArticleSearchLogMapper;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.service.ArmArticleSearchLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源检索日志 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-15
 */
@Service
public class ArmArticleSearchLogServiceImpl extends ServiceImpl<ArmArticleSearchLogMapper, ArmArticleSearchLog> implements ArmArticleSearchLogService {

 @Override
 public List<Map> getRecords(Long id, String tableName, Date startTime, Date endTime, Page pageParams) {
  return baseMapper.getRecords(id,tableName,pageParams.getCurrent(),pageParams.getSize(),startTime,endTime);
 }

 @Override
 public List<Map> getRecords(Long id, Date startTime, Date endTime, Page pageParams) {
  return baseMapper.getAllTypeRecords(id,pageParams.getCurrent(),pageParams.getSize(),startTime,endTime);
 }

 @Override
 public Integer countRecords(Long id, Date startTime, Date endTime) {
  return baseMapper.countRecords(id,startTime,endTime);
 }

 @Override
 public Integer countRecords(Long id, String tableName, Date startTime, Date endTime) {
  return baseMapper.countRecordsByTableName(id,startTime,endTime,tableName);
 }
}
