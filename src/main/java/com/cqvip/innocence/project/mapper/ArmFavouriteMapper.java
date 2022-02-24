package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmFavourite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户收藏表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
public interface ArmFavouriteMapper extends BaseMapper<ArmFavourite> {

 List<Map> statCollectTrend(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("type") String type);
}
