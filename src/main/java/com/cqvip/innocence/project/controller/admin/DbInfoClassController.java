package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.annotation.SensitiveTag;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.DbInfoClass;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.DbInfoClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据库分类表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/db-info-class")
@Api(tags="管理端数据库分类管理")
public class DbInfoClassController extends AbstractController {

    @Autowired
    private DbInfoClassService classService;

    @PostMapping("addOrEdit")
    @ApiOperation("新增编辑数据库分类")
    @Log(title = "新增编辑数据库分类", operateType = VipEnums.OperateType.SAVE_OR_UPDATE)
    @SensitiveTag
    public JsonResult addOrEdit(DbInfoClass dbInfoClass) {
        return JsonResult.Get(classService.saveOrUpdate(dbInfoClass));
    }

    @GetMapping("getPageList")
    @ApiOperation("分页查询数据库分类")
    @Log(title = "查询数据库分类分页列表", operateType = VipEnums.OperateType.SEARCH)
    public JsonResult getPageList(DbInfoClass dbInfoClass) {
        LambdaQueryWrapper<DbInfoClass> lambda = new QueryWrapper<DbInfoClass>().lambda()
                .orderByDesc(DbInfoClass::getCreateTime);
        if (StrUtil.isNotBlank(dbInfoClass.getTitle())) {
            lambda.like(DbInfoClass::getTitle, dbInfoClass.getTitle());
        }
        Page pageParams = getPageParams();
        return JsonResult.Get().putPage(classService.page(pageParams, lambda));
    }

    @GetMapping("getList")
    @ApiOperation("获取数据库分类列表，不分页")
    public JsonResult getList(){
        List<DbInfoClass> list = classService.list(new QueryWrapper<DbInfoClass>().lambda().orderByDesc(DbInfoClass::getModifyTime));
        return JsonResult.Get().putList(list);
    }

    @PostMapping("deleteById")
    @ApiOperation("根据id删除数据库分类，支持批量删除")
    @Log(title = "删除数据库分类", operateType = VipEnums.OperateType.DELETE)
    public JsonResult deleteById(@RequestBody List<Serializable> ids){
        return JsonResult.Get(classService.removeByIds(ids));
    }

}

