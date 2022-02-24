package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 提问表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmQuestionMapper extends BaseMapper<ArmQuestion> {
    /**
     * 分页查询提问表
     * @param armQuestion 条件
     * @param page 分页条件
     * @return {@link IPage<Map<String,Object>>}
     * @author 01
     * @date 2021/8/23 11:22
     */
    IPage<ArmQuestion> getArmQuestionByPage(@Param("armQuestion") ArmQuestion armQuestion,Page page);
}
