package com.cqvip.innocence.project.controller.admin;


import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmScoreOption;
import com.cqvip.innocence.project.service.ArmScoreOptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 积分配置表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-10-08
 */
@RestController
@RequestMapping("/${base-url.manager}/armScoreOption")
public class ArmScoreOptionController extends AbstractController {

    @Autowired
    ArmScoreOptionService armScoreOptionService;

    @ApiOperation("获取全部积分配置")
    @GetMapping("/getList")
    public JsonResult getList(){
        List<ArmScoreOption> list = armScoreOptionService.list();
        return JsonResult.Get().putList(list);
    }

    @ApiOperation("批量修改积分配置")
    @PostMapping("/batchUpdate")
    public JsonResult batchUpdate(@RequestBody List<ArmScoreOption> list){
        try{
            armScoreOptionService.saveOrUpdateBatch(list);
        }catch(Exception ex){
            log.info("修改积分配置异常");
            return JsonResult.Get(false);
        }
        return JsonResult.Get();
    }

}

