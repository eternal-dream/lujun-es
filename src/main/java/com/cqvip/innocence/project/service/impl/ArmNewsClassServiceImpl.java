package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.entity.ArmNewsClass;
import com.cqvip.innocence.project.mapper.ArmNewsClassMapper;
import com.cqvip.innocence.project.service.ArmNewsClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 发布内容分类表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Service
public class ArmNewsClassServiceImpl extends ServiceImpl<ArmNewsClassMapper, ArmNewsClass> implements ArmNewsClassService {

    @Override
    public List<ArmNewsClass> getSoftwareClassByColumnName(String columnName) {
        List<ArmNewsClass> armNewsClassList = baseMapper.getSoftwareClassByColumnName(columnName);
        return armNewsClassList;
    }
}
