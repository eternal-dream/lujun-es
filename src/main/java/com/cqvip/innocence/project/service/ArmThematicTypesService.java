package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.ArmThematicTypes;

import java.util.Map;

/**
 * <p>
 * 专题库类型表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
public interface ArmThematicTypesService extends IService<ArmThematicTypes> {
    IPage<ArmThematicTypes> getThematicTypesByPage(ArmThematicTypes armThematicTypes, Page page);

}
