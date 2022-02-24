package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Description 前台的首页接口
 * @Author Innocence
 * @Date 2021/10/15 11:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/index/")
@Api(tags = "前台首页相关接口")
public class IndexController extends AbstractController {

    @Autowired
    private ArmNewJournalArticleService articleService;

    @Autowired
    private ArmArticleSearchLogService searchLogService;

    @Autowired
    private DocumentService<ArticleDoc> documentService;

    @Autowired
    private ArmNewsService newsService;

    @Autowired
    private DbInfoUpdateLogService updateLogService;

    @Autowired
    private DbInfoService dbInfoService;

    @GetMapping("getArticleByJournal")
    @ApiOperation("获取新刊简目推荐文献（按添加时间取6条）")
    public JsonResult getArticleByJournal(){
        LambdaQueryWrapper<ArmNewJournalArticle> last = new LambdaQueryWrapper<ArmNewJournalArticle>()
                .orderByDesc(ArmNewJournalArticle::getCreateTime)
                .last("limit 6");
        return JsonResult.Get().putList(articleService.list(last));
    }

    @GetMapping("getHotRecom")
    @ApiOperation("获取热门推荐（搜索最多的）")
    public JsonResult getHotAndHistoryRecom(){
        QueryWrapper<ArmArticleSearchLog> byDesc = new QueryWrapper<ArmArticleSearchLog>()
                .select("COUNT(DISPLAY_INFO)", "DISPLAY_INFO")
                .groupBy("DISPLAY_INFO")
                .orderByDesc("COUNT(DISPLAY_INFO)");
        List<Map<String, Object>> maps = searchLogService.listMaps(byDesc);
        String displayInfo = (String)maps.get(0).get("DISPLAY_INFO");
        LambdaQueryWrapper<ArmArticleSearchLog> eq = new LambdaQueryWrapper<ArmArticleSearchLog>()
                .eq(ArmArticleSearchLog::getDisplayInfo, displayInfo)
                .isNotNull(ArmArticleSearchLog::getSearchCondition)
                .last("limit 1");
        ArmArticleSearchLog one = searchLogService.getOne(eq);
        BoolQueryBuilder ikSmart = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(one.getSearchCondition()).analyzer("ik_smart")).boost(2.0f);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(ikSmart)
                .withPageable(PageRequest.of(0, 5))
                .build();
        Map<String, Object> pageList = documentService.getPageList(build, PageRequest.of(0, 5), ArticleDoc.class);
        return JsonResult.Get().putPage(pageList);
    }

    @GetMapping("getNewsToIndex")
    @ApiOperation("首页获取新闻公告")
    public JsonResult getNewsToIndex(){
        return JsonResult.Get().putList(newsService.getNewsListToIndex());
    }

    @GetMapping("getUpdateLog")
    @ApiOperation("首页获取资源更新记录")
    public JsonResult getUpdateLog(){
        List<DbInfoUpdateLog> list = updateLogService.getLimitList();
        return JsonResult.Get().putList(list);
    }

    @GetMapping("getKeyResources")
    @ApiOperation("获取重点资源列表")
    public JsonResult getKeyResources(){
        List<DbInfo> list = dbInfoService.list(new LambdaQueryWrapper<DbInfo>()
                .orderByDesc(DbInfo::getCreateTime)
                .last("limit 0,12"));
        return JsonResult.Get().putList(list);
    }
}
