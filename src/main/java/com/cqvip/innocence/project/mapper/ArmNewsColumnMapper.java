package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.entity.ArmNewsColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 新闻栏目表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface ArmNewsColumnMapper extends BaseMapper<ArmNewsColumn> {
    /**
     * 获取所有非一级栏目
     *
     * @return {@link List< ArmNewsColumn>}
     * @author 01
     * @date 2021/9/7 13:29
     */
    List<ArmNewsColumn> getAllNotSuperNewsColumn();
    /**
     * 获取所有一级栏目
     *
     * @return {@link List< ArmNewsColumn>}
     * @author 01
     * @date 2021/9/7 13:29
     */
    List<ArmNewsColumn> getAllSuperNewsColumn();
}
