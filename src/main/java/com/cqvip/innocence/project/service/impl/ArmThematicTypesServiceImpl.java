package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.ArmThematicTypesMapper;
import com.cqvip.innocence.project.model.entity.ArmThematicTypes;
import com.cqvip.innocence.project.service.ArmThematicTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 专题库类型表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@Service
public class ArmThematicTypesServiceImpl extends ServiceImpl<ArmThematicTypesMapper, ArmThematicTypes> implements ArmThematicTypesService {

    @Autowired
    ArmThematicTypesMapper armThematicTypesMapper;

    @Override
    public IPage<ArmThematicTypes> getThematicTypesByPage(ArmThematicTypes armThematicTypes, Page page) {
        return armThematicTypesMapper.getThematicTypesByPage(armThematicTypes,page);
    }


}
