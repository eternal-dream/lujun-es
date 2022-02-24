package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmArticleSearchLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.base.BaseModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源检索日志 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-15
 */
public interface ArmArticleSearchLogService extends IService<ArmArticleSearchLog> {

 /**
  * 数字资源应用记录
  * @param id
  * @param tableName 表名
  * @param startTime
  * @param endTime
  * @param pageParams
  * @return
  */
 List<Map> getRecords(Long id, String tableName, Date startTime, Date endTime, Page pageParams);

 /**
  * 数字资源应用记录(全部)
  * @param id
  * @param startTime
  * @param endTime
  * @param pageParams
  * @return
  */
 List<Map> getRecords(Long id, Date startTime, Date endTime, Page pageParams);

 Integer countRecords(Long id, Date startTime, Date endTime);

 Integer countRecords(Long id, String tableName, Date startTime, Date endTime);
}
