package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.ArmQuestion;
import com.cqvip.innocence.project.model.enums.VipEnums;

import java.util.Map;

/**
 * <p>
 * 提问表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmQuestionService extends IService<ArmQuestion> {
    /**
     * 获取提问表分页列表数据
     *
     * @param armQuestion
     * @param page
     * @return {@link IPage< Map< String, Object>>}
     * @author 01
     * @date 2021/8/30 13:44
     */
    IPage<ArmQuestion> getArmQuestionByPage(ArmQuestion armQuestion, Page page);
    /**
     * 更新提问表数据
     *
     * @param questionId 问题id
     * @param title 标题
     * @param classId 提问类型id
     * @param content 问题内容
     * @param answerContent 回答内容
     * @param type
     * @return {@link boolean}
     * @author 01
     * @date 2021/8/30 13:44
     */
    boolean updateArmQuestion(Long questionId,String title, Long classId, String content, String answerContent, VipEnums.QuestionTypeEnum type);
    /**
     *  保存提问表数据
     *
     * @param title 标题
     * @param classId 提问类型id
     * @param content 问题内容
     * @param answerContent 回答内容
     * @param type 类型
     * @return {@link boolean}
     * @author 01
     * @date 2021/8/30 13:46
     */
    boolean saveArmQuestion(String title, Long classId, String content, String answerContent, VipEnums.QuestionTypeEnum type);

}
