package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.mapper.ArmAnswerMapper;
import com.cqvip.innocence.project.mapper.ArmQuestionMapper;
import com.cqvip.innocence.project.model.entity.ArmAnswer;
import com.cqvip.innocence.project.model.entity.ArmQuestion;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 提问表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ArmQuestionServiceImpl extends ServiceImpl<ArmQuestionMapper, ArmQuestion> implements ArmQuestionService {

    @Autowired
    ArmQuestionMapper armQuestionMapper;

    @Autowired
    ArmAnswerMapper armAnswerMapper;


    @Override
    public IPage<ArmQuestion> getArmQuestionByPage(ArmQuestion armQuestion, Page page) {
        try {
            return armQuestionMapper.getArmQuestionByPage(armQuestion, page);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean saveArmQuestion(String title, Long classId, String content, String answerContent, VipEnums.QuestionTypeEnum type) {
        //TODO FIX the adminId
        //ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
        //armQuestion.setAdminId(currentAdmin.getId());
        ArmQuestion armQuestion = new ArmQuestion();
        armQuestion.setTitle(title);
        armQuestion.setClassId(classId);
        armQuestion.setContent(content);
        armQuestion.setAdminId(111L);
        armQuestion.setType(type);
        ArmAnswer armAnswer = new ArmAnswer();
        try {
            armQuestionMapper.insert(armQuestion);

            Long questionId = armQuestion.getId();
            armAnswer.setQuestionId(questionId);
            armAnswer.setAdminId(111L);
            armAnswerMapper.insert(armAnswer);
        } catch (Exception ex) {
            log.info("新增提问表数据时发生异常，原因是：{}", ex.getMessage());
            return false;
        }
        return true;
    }


    @Override
    public boolean updateArmQuestion(Long questionId, String title, Long classId, String content, String answerContent, VipEnums.QuestionTypeEnum type) {
        ArmQuestion armQuestion = armQuestionMapper.selectById(questionId);
        if (armQuestion == null) {
            return false;
        }
        armQuestion.setType(type);
        armQuestion.setContent(content);
        armQuestion.setClassId(classId);
        armQuestion.setTitle(title);

        QueryWrapper<ArmAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("QUESTION_ID", questionId);
        ArmAnswer armAnswer = armAnswerMapper.selectOne(queryWrapper);
        armAnswer.setContent(content);
        try {
            armQuestionMapper.updateById(armQuestion);
            armAnswerMapper.updateById(armAnswer);
        } catch (Exception ex) {
            log.info("修改提问表数据时发生异常，原因是：{}", ex.getMessage());
            return false;
        }
        return true;
    }
}
