package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmQuestion;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmQuestionService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 提问表(常见问题) 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/armQuestion")
public class ArmQuestionController extends AbstractController {

    @Autowired
    ArmQuestionService armQuestionService;

    /**
     * 获取问题列表
     *
     * @param armQuestion
     * @param page
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/23 11:01
     */
    @GetMapping("/getQuestionByPage")
    @ApiOperation("获取问题列表（分页）")
    public JsonResult getArmQuestionByPage(ArmQuestion armQuestion, Page page) {
        IPage<ArmQuestion> questionPage = null;
        try {
            questionPage = armQuestionService.getArmQuestionByPage(armQuestion, page);
        } catch (Exception ex) {
            return JsonResult.Get(false).put("data", null);
        }
        return JsonResult.Get().put("questionPage", questionPage);
    }

    @GetMapping("/getArmQuestionDetailById/{id}")
    @ApiOperation("通过id获取问题详情信息")
    public JsonResult getArmQuestionDetailById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        ArmQuestion armQuestion = null;
        try {
            armQuestion = armQuestionService.getById(id);
        } catch (Exception ex) {
            return JsonResult.Get(false).put("data", null);
        }
        return JsonResult.Get().put("data", armQuestion);
    }

    @PostMapping("addOrModifyQuestion")
    public JsonResult addOrModifyQuestion(ArmQuestion question){
        question.setType(VipEnums.QuestionTypeEnum.QUESTION);
        ArmAdminUser loginAdmin = (ArmAdminUser) SecurityUtils.getSubject().getSession().getAttribute(SessionKeys.LOGIN_ADMIN_KEY);
        if(question.getAdminId() == null){
            question.setAdminId(loginAdmin.getId());
        }
        if(StringUtils.isNotBlank(question.getAnswer())){
            question.setAdminId(loginAdmin.getId());
        }
        return JsonResult.Get(question.insertOrUpdate());
    }

    @PostMapping("deleteQuestion")
    public JsonResult deleteQuestion(@RequestBody Long[] ids){
        return JsonResult.Get(armQuestionService.removeByIds(Arrays.asList(ids)));
    }

}

