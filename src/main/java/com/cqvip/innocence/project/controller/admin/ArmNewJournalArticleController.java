package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmNewJournalArticle;
import com.cqvip.innocence.project.service.ArmNewJournalArticleService;
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
 * 新刊发布内容表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/${base-url.manager}/arm-new-journal-article/")
@Api(tags = "管理员对期刊文献的管理")
public class ArmNewJournalArticleController extends AbstractController {

    @Autowired
    private ArmNewJournalArticleService articleService;

    @GetMapping("getPageList")
    @ApiOperation("根据期刊id获取文献分页列表")
    public JsonResult getPageList(Long journalId, String title){
        Page pageParams = getPageParams();
        if (null == journalId){
            return JsonResult.Get(false,"期刊id不能为空");
        }
        return JsonResult.Get().putPage(articleService.getPageList(pageParams,journalId,title));
    }

    @XssExclusion
    @PostMapping("addOrEdit")
    @ApiOperation("新增编辑期刊文献")
    public JsonResult addOrEdit(ArmNewJournalArticle article){
        Subject subject = SecurityUtils.getSubject();
        ArmAdminUser principal =(ArmAdminUser) subject.getPrincipal();
        article.setUserId(principal.getId());
        return JsonResult.Get(articleService.saveOrUpdate(article));
    }

    @PostMapping("deleteByIds")
    @ApiOperation("根据id删除新刊文献")
    public JsonResult deleteByIds(@RequestBody List<Serializable> ids){
        boolean remove = articleService.removeByIds(ids);
        return JsonResult.Get(remove);
    }

}

