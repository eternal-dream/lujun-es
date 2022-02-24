package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.SensitiveTag;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.DbInfoResource;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.DbInfoResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资源分类信息 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/db-info-resource")
@Api(tags="管理端资源分类管理（数据库的资源分类）")
public class DbInfoResourceController extends AbstractController {

    @Autowired
    private DbInfoResourceService dbInfoResourceService;

    @PostMapping("addOrEdit")
    @ApiOperation("新增编辑资源分类")
    @SensitiveTag
    public JsonResult addOrEdit(DbInfoResource resource){
        if (StrUtil.isBlank(resource.getTitle())){
            return JsonResult.Get(false,"资源名不能为空！");
        }
        if (resource.getType() == null){
            return JsonResult.Get(false,"类型不能为空！");
        }
        return JsonResult.Get(dbInfoResourceService.saveOrUpdate(resource));
    }

    @GetMapping("getPageList")
    @ApiOperation("获取资源分类分页列表,支持资源名模糊检索")
    public JsonResult getPageList(String title, @NotNull VipEnums.ResourceType type){
        LambdaQueryWrapper<DbInfoResource> lambda = new QueryWrapper<DbInfoResource>().lambda()
                .eq(DbInfoResource::getType,type)
                .orderByDesc(DbInfoResource::getSortId);
        if (StrUtil.isNotBlank(title)){
            lambda.like(DbInfoResource::getTitle, title);
        }
        Page pageParams = getPageParams();
        return JsonResult.Get().putPage(dbInfoResourceService.page(pageParams,lambda));
    }

    @GetMapping("getListGroupByType")
    @ApiOperation("获取资源列表并按类型分组")
    public JsonResult getListByType(){
        LambdaQueryWrapper<DbInfoResource> wrapper = new QueryWrapper<DbInfoResource>().lambda()
                .orderByDesc(DbInfoResource::getSortId);
        List<DbInfoResource> list = dbInfoResourceService.list(wrapper);
        List<DbInfoResource> types = new ArrayList<>();
        List<DbInfoResource> contents = new ArrayList<>();
        List<DbInfoResource> systems = new ArrayList<>();
        list.forEach(item ->{
            if (item.getType().equals(VipEnums.ResourceType.TYPE)){
                types.add(item);
            }else if (item.getType().equals(VipEnums.ResourceType.CONTENT)){
                contents.add(item);
            }else {
                systems.add(item);
            }
        });
        return JsonResult.Get()
                .put(VipEnums.ResourceType.TYPE.name(),types)
                .put(VipEnums.ResourceType.CONTENT.name(),contents)
                .put(VipEnums.ResourceType.SYSTEM.name(),systems);
    }

    @PostMapping("deleteById")
    @ApiOperation("根据ID删除资源分类，支持批量删除")
    public JsonResult deleteById(@RequestBody List<Serializable> ids){
        return JsonResult.Get(dbInfoResourceService.removeByIds(ids));
    }

}

