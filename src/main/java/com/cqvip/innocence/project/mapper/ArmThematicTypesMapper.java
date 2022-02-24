package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmThematicTypes;

import java.util.Map;

/**
 * <p>
 * 专题库类型表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
public interface ArmThematicTypesMapper extends BaseMapper<ArmThematicTypes> {
    /**
     * 专题库类型列表
     * @param armThematicTypes
     * @param page
     * @return
     */
    IPage<ArmThematicTypes> getThematicTypesByPage(ArmThematicTypes armThematicTypes, Page page);

}
