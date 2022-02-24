package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNewJournal;
import com.cqvip.innocence.project.model.entity.ArmNewJournalArticle;
import com.cqvip.innocence.project.model.entity.ArmNewJournalClass;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.service.ArmNewJournalArticleService;
import com.cqvip.innocence.project.service.ArmNewJournalClassService;
import com.cqvip.innocence.project.service.ArmNewJournalService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @ClassName NewJournalController
 * @Description TODO
 * @Author Innocence
 * @Date 2021/10/20 15:01
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/newJournal/")
public class NewJournalController extends AbstractController {

    @Autowired
    private ArmNewJournalClassService classService;

    @Autowired
    private ArmNewJournalArticleService articleService;

    @Autowired
    private ArmNewJournalService journalService;

    @NoLogin
    @GetMapping("getClassAndChildren")
    @ApiOperation("获取新刊分类，以及当前分类下的新刊列表")
    public JsonResult getClassAndChildren(){
        LambdaQueryWrapper<ArmNewJournalClass> classWrapper = new LambdaQueryWrapper<ArmNewJournalClass>().orderByDesc(ArmNewJournalClass::getCreateTime);
        List<ArmNewJournalClass> classList = classService.list(classWrapper);
        LambdaQueryWrapper<ArmNewJournal> journalWrapper = new LambdaQueryWrapper<ArmNewJournal>().orderByDesc(ArmNewJournal::getCreateTime);
        List<ArmNewJournal> journalList = journalService.list(journalWrapper);
        classList.forEach(clazz ->{
            ArrayList<ArmNewJournal> journals = new ArrayList<>();
            journalList.forEach(journal -> {
                if (journal.getClassId().equals(clazz.getId())){
                    journals.add(journal);
                }
            });
            clazz.setChildrenList(journals);
        });
        return JsonResult.Get().putList(classList);
    }

    @GetMapping("getArticleListById")
    @ApiOperation("根据新刊id获取该刊下的文献分页")
    public JsonResult getArticleListById(Long id){
        return JsonResult.Get().putPage(articleService.getPageList(getPageParams(),id,null));
    }

    @GetMapping("getArticleDetailById")
    @ApiOperation("根据id获取文献详情")
    public JsonResult getArticleDetailById(Long id){
        return JsonResult.Get().putRes(articleService.getDetailById(id));
    }

    /**
     * 新刊推荐
     * @return
     */
    @GetMapping("getNewJournalArticles")
    public JsonResult getNewJournalArticles(){
        List<Map> newJournalArticles = articleService.getNewJournalArticles(getPageParams());
        return JsonResult.Get("newJournalArticles",newJournalArticles);
    }
}
