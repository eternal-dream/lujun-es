package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmQuestion;
import com.cqvip.innocence.project.service.ArmQuestionClassService;
import com.cqvip.innocence.project.service.ArmQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 常见问题
 * @Author eternal
 * @Date 2021/9/17
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/question")
public class QuestionController extends AbstractController {

 @Autowired
 private ArmQuestionService questionService;

 @Autowired
 private ArmQuestionClassService questionClassService;

 @GetMapping("getAllQuestionClasses")
 public JsonResult getAllQuestionClasses(){
  return JsonResult.Get("questionClasses",questionClassService.list());
 }

 @GetMapping("getQuestionsByClassId")
 @Log(title = "查看常见问题")
 public JsonResult getQuestionsByClassId(Long classId){
  if(classId == null){
   return JsonResult.Get(false);
  }
  LambdaQueryWrapper<ArmQuestion> wrapper = new LambdaQueryWrapper();
  wrapper.eq(ArmQuestion::getClassId,classId);
  List<ArmQuestion> questions = questionService.list(wrapper);
  return JsonResult.Get("questions",questions);
 }



}