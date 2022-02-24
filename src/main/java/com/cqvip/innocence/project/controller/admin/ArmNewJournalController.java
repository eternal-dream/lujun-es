package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNewJournal;
import com.cqvip.innocence.project.service.ArmNewJournalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 新刊表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/${base-url.manager}/arm-new-journal/")
@Api(tags = "管理员对期刊的管理接口")
public class ArmNewJournalController extends AbstractController {

    @Autowired
    private ArmNewJournalService journalService;

    @GetMapping("getPageList")
    @ApiOperation("获取期刊分页数据（分类参数必填），支持期刊名模糊检索")
    public JsonResult getPageList(ArmNewJournal journal){
        if (null == journal.getClassId()){
            return JsonResult.Get(false,"分类参数不能为空！");
        }
        LambdaQueryWrapper<ArmNewJournal> lambda = new QueryWrapper<ArmNewJournal>().lambda()
                .eq(ArmNewJournal::getClassId,journal.getClassId())
                .orderByDesc(ArmNewJournal::getModifyTime);
        if (StrUtil.isNotBlank(journal.getTitle())){
            lambda.like(ArmNewJournal::getTitle,journal.getTitle());
        }
        Page pageParams = getPageParams();
        Page page = journalService.page(pageParams, lambda);
        return JsonResult.Get().putPage(page);
    }

    @GetMapping("getListWithOutPage")
    @ApiOperation("获取新刊列表，不分页")
    public JsonResult getListWithOutPage(){
        return JsonResult.Get().putList(journalService.list());
    }

    @PostMapping("addOrEdit")
    @ApiOperation("新增编辑新刊数据信息")
    public JsonResult addOrEdit(ArmNewJournal journal){
        boolean update = journalService.saveOrUpdate(journal);
        return JsonResult.Get(update);
    }

    @PostMapping("deleteByIds")
    @ApiOperation("根据id删除期刊信息，期刊下面的文献会全部被删除")
    public JsonResult deleteByIds(@RequestBody List<Serializable> ids){
        return JsonResult.Get(journalService.deleteByIds(ids));
    }

}

