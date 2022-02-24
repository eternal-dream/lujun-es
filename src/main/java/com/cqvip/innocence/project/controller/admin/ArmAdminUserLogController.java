package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.util.enums.EnumUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUserLog;
import com.cqvip.innocence.project.service.ArmAdminUserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

/**
 * <p>
 * 管理员操作记录表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@RestController
@RequestMapping("/${base-url.manager}/armAdminUserLog")
@Api(tags = "管理员操作记录接口")
public class ArmAdminUserLogController extends AbstractController {

    @Autowired
    private ArmAdminUserLogService userLogService;

    @GetMapping("getLogPage")
    @ApiOperation("获取管理员操作日志分页")
    public JsonResult getPageList(ArmAdminUserLog userLog){
        LambdaQueryWrapper<ArmAdminUserLog> lambda = new QueryWrapper<ArmAdminUserLog>().lambda();
        if (StrUtil.isNotBlank(userLog.getOperatName())){
            lambda.like(ArmAdminUserLog::getOperatName,userLog.getOperatName());
        }
        if (userLog.getOperateType() != null){
            lambda.eq(ArmAdminUserLog::getOperateType, userLog.getOperateType());
        }
        Page pageParams = getPageParams();
        Page page = userLogService.page(pageParams, lambda);
        return JsonResult.Get().putPage(page);
    }

}

