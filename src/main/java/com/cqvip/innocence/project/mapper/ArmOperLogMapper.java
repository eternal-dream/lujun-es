package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员操作日志 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
public interface ArmOperLogMapper extends BaseMapper<ArmOperLog> {

 List<Map> getVisitCountOfYear();

 int countVisitTimes(@Param("limit") Date limit);

 List<Map> getVisitCountOfMonth();

 List<Map> getVisitCountOfWeek(@Param("monday")Date monday);

 List<Map> getVisitCountOfDay();
}
