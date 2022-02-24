package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmThematicDatabase;

import java.util.Map;

/**
 * <p>
 * 专题数据库 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
public interface ArmThematicDatabaseMapper extends BaseMapper<ArmThematicDatabase> {
    /**
     * 获取热点主题类型（分页）
     *
     * @param armThematicDatabase
     * @param page
     * @return {@link IPage< Map< String, Object>>}
     * @throws
     * @author 01
     * @date 2021/8/30 13:41
     */
    IPage<ArmThematicDatabase> getThematicTypesByPage(ArmThematicDatabase armThematicDatabase, Page page);
}
