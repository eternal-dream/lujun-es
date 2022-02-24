package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNewsClass;
import com.cqvip.innocence.project.service.ArmNewsClassService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 发布内容分类表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/armNewsClass")
public class ArmNewsClassController extends AbstractController {
    @Autowired
    ArmNewsClassService armNewsClassService;

    @GetMapping("/getNewsClassByPage")
    @ApiOperation("获取“发布内容类型”分页列表")
    public JsonResult getNewsClassByPage(ArmNewsClass armNewsClass, Page page) {
        IPage<ArmNewsClass> pageParams = new Page<>(page.getCurrent(), page.getSize());
        QueryWrapper<ArmNewsClass> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(armNewsClass.getTitle())){
            queryWrapper = queryWrapper.like("TITLE", armNewsClass.getTitle());
        }
        if(armNewsClass.getColumnId()!=null){
            queryWrapper = queryWrapper.eq("COLUMN_ID", armNewsClass.getColumnId());
        }
        IPage<ArmNewsClass> data = armNewsClassService.page(pageParams, queryWrapper.orderByDesc("CREATE_TIME"));
        return JsonResult.Get().put("data", data);
    }


    @GetMapping("/getNewsClassById/{id}")
    @ApiOperation("通过id查询新闻类型信息")
    public JsonResult getNewsClassById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        ArmNewsClass armNews = armNewsClassService.getById(id);
        return JsonResult.Get().put("data", armNews);
    }

    @PostMapping("/deleteNewsClassById")
    @ApiOperation("通过id删除“发布内容类型”")
    public JsonResult deleteNewsClassById(Long[] ids) {
        if (ids == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }

        try {
            armNewsClassService.removeByIds(Arrays.asList(ids));
        } catch (Exception ex) {
            log.info("通过id删除“发布内容类型”信息失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

    @PostMapping("/saveOrUpdateNewsClass")
    @ApiOperation("添加或修改“发布内容类型”")
    public JsonResult saveOrUpdateNewsClass(@RequestBody ArmNewsClass armNewsClass) {
        if (armNewsClass == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armNewsClassService.saveOrUpdate(armNewsClass);
        } catch (Exception ex) {
            log.info("添加或修改“发布内容类型”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }


}

