package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNewJournalClass;
import com.cqvip.innocence.project.service.ArmNewJournalClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 新刊简目分类 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/${base-url.manager}/arm-new-journal-class/")
@Api(tags = "管理员对期刊分类的管理接口")
public class ArmNewJournalClassController extends AbstractController {

    @Autowired
    private ArmNewJournalClassService classService;

    @GetMapping("getPageList")
    @ApiOperation("获取新刊分类的分页数据,支持分类名模糊检索,默认按修改时间倒序排列")
    public JsonResult getPageList(ArmNewJournalClass journalClass){
        Page page = getPageParams();
        LambdaQueryWrapper<ArmNewJournalClass> lambda = new QueryWrapper<ArmNewJournalClass>()
                .lambda().orderByDesc(ArmNewJournalClass::getModifyTime);
        if (StrUtil.isNotBlank(journalClass.getTitle())){
            lambda.like(ArmNewJournalClass::getTitle,journalClass.getTitle());
        }
        return JsonResult.Get().putPage(classService.page(page, lambda));
    }

    @GetMapping("getListWithOutPage")
    @ApiOperation("获取新刊分类列表，不分页，不排序")
    public JsonResult getListWithOutPage(){
        return JsonResult.Get().putList(classService.list());
    }

    @PostMapping("addOrEdit")
    @ApiOperation("新增编辑新刊分类数据")
    public JsonResult addOrEdit(ArmNewJournalClass journalClass){
        // 分类的level和parentId暂时未使用，默认值为0
        journalClass.setLevel(0);
        journalClass.setParentId(0L);
        boolean b = classService.saveOrUpdate(journalClass);
        return JsonResult.Get(b);
    }

    @PostMapping("deleteByIds")
    @ApiOperation("根据id删除新刊分类,会删掉分类下面的所有新刊和新刊文献")
    public JsonResult deleteByIds(@RequestBody List<Serializable> ids){
        return JsonResult.Get(classService.deleteByIds(ids));
    }
}

