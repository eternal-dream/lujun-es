package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmFavourite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户收藏表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
public interface ArmFavouriteService extends IService<ArmFavourite> {

 /**
  * 按年/月查询收藏趋势
  * @param beginTime
  * @param endTime
  * @param type
  * @return
  */
 List<Map> statCollectTrend(Date beginTime, Date endTime, String type);
}
