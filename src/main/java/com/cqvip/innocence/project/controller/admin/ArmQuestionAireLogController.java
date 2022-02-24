package com.cqvip.innocence.project.controller.admin;


import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmQuestionAireLog;
import com.cqvip.innocence.project.service.ArmQuestionAireLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

/**
 * <p>
 * 问卷调查日志表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/armQuestionAireLog")
public class ArmQuestionAireLogController extends AbstractController {
    @Autowired
    ArmQuestionAireLogService armQuestionAireLogService;

    @PostMapping("/saveQuestionAireLog")
    @ApiOperation("保存问卷调查日志信息")
    public JsonResult saveQuestionAireLog(ArmQuestionAireLog armQuestionAireLog){
        if(armQuestionAireLog==null){

        }
        //TODO ipAddress
        //armQuestionAireLog.setIp()
        try{
            armQuestionAireLogService.save(armQuestionAireLog);
        }catch(Exception ex){
            log.info("新增“问卷日志”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get();
    }


}

