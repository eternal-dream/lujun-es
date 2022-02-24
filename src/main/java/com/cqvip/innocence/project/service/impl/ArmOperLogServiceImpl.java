package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.ArmOperLog;
import com.cqvip.innocence.project.mapper.ArmOperLogMapper;
import com.cqvip.innocence.project.service.ArmOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员操作日志 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@Service
public class ArmOperLogServiceImpl extends ServiceImpl<ArmOperLogMapper, ArmOperLog> implements ArmOperLogService {

    @Override
    public List<Map> getVisitCountOfYear() {
        List<Map> visitCountOfYear = baseMapper.getVisitCountOfYear();
        visitCountOfYear = fillData(visitCountOfYear, 1, LocalDate.now().getMonthValue());
        return visitCountOfYear;
    }

    private List<Map> fillData(List<Map> visitCount, Integer begin, Integer end) {
        for (int i = begin; i <= end; i++) {
            boolean exists = false;
            for (Map map : visitCount) {
                if ((i + "").equals(map.get("DATE").toString())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Map tempData = new HashMap();
                tempData.put("COUNT", 0);
                tempData.put("DATE", i);
                visitCount.add(tempData);
            }
        }
        visitCount = visitCount.stream().sorted(Comparator.comparingInt(v -> Integer.parseInt(v.get("DATE").toString())))
                .collect(Collectors.toList());
        return visitCount;
    }

    @Override
    public int countVisitTimes(Date limit) {
        return baseMapper.countVisitTimes(limit);
    }

    @Override
    public List<Map> getVisitCountOfMonth() {
        List<Map> visitCountOfMonth = baseMapper.getVisitCountOfMonth();
        visitCountOfMonth = fillData(visitCountOfMonth, 1, LocalDate.now().getDayOfMonth());
        return visitCountOfMonth;
    }

    @Override
    public List<Map> getVisitCountOfWeek() throws ParseException {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map> visitCountOfWeek = baseMapper.getVisitCountOfWeek(sdf.parse(monday.toString()));
        visitCountOfWeek = fillData(visitCountOfWeek, LocalDate.now().with(DayOfWeek.MONDAY).getDayOfMonth(), LocalDate.now().getDayOfMonth());
        return visitCountOfWeek;
    }

    @Override
    public List<Map> getVisitCountOfDay() {
        List<Map> visitCountOfDay = baseMapper.getVisitCountOfDay();
        visitCountOfDay = fillData(visitCountOfDay, 1, LocalDateTime.now().getHour());
        return visitCountOfDay;
    }
}
