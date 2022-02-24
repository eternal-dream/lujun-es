package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.DbInfoUpdateLog;
import com.cqvip.innocence.project.model.enums.BusinessType;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.DbInfoUpdateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据库资源更新日志表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@RestController
@RequestMapping("/${base-url.manager}/db-info-update-log")
@Api(tags = "管理端的数据库资源更新日志管理接口")
public class DbInfoUpdateLogController extends AbstractController {

    @Autowired
    private DbInfoUpdateLogService updateLogService;

    @PostMapping("add")
    @ApiOperation("管理员新增数据库更新日志")
    public JsonResult add(DbInfoUpdateLog updateLog){
        Subject subject = SecurityUtils.getSubject();
        ArmAdminUser principal =(ArmAdminUser) subject.getPrincipal();
        updateLog.setUserId(principal.getId());
        boolean save = updateLogService.save(updateLog);
        return JsonResult.Get(save);
    }

    @GetMapping("getPageList")
    @ApiOperation("管理员获取数据库更新日志分页")
    public JsonResult getPageList(Long dbId){
        Page<DbInfoUpdateLog> pageList = updateLogService.getPageList(getPageParams(), dbId);
        return JsonResult.Get().putPage(pageList);
    }

    @PostMapping("deleteById")
    @ApiOperation("根据ID删除数据库更新日志，支持批量删除")
    public JsonResult deleteById(@RequestBody List<Serializable> ids){
        return JsonResult.Get(updateLogService.removeByIds(ids));
    }
}

