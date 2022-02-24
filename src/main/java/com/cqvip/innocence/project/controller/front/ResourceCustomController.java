package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.service.ArmArticleVisitLogService;
import com.cqvip.innocence.project.service.ArmClcClassInfoService;
import com.cqvip.innocence.project.service.ArmUserCustomizeService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author eternal
 * @Date 2021/10/8
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/resourceCustom")
public class ResourceCustomController extends AbstractController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArmArticleVisitLogService articleVisitLogService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ArmUserCustomizeService userCustomizeService;

    @Autowired
    private ArmClcClassInfoService clcClassInfoService;

    /**
     * 足迹推荐
     *
     * @return
     */
    @GetMapping("footprintRecommend")
    public JsonResult footprintRecommend(HttpServletRequest request) throws IOException {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
        //查询用户近一月浏览过的文章id集合
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        Date time = calendar.getTime();
        LambdaQueryWrapper<ArmArticleVisitLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmArticleVisitLog::getUserId, user.getId())
                .ge(BaseModel::getCreateTime, time)
                .select(ArmArticleVisitLog::getObjId);
        List<ArmArticleVisitLog> visitLogs = articleVisitLogService.list(wrapper);
        List<String> ids = new ArrayList<>();
        visitLogs.forEach(log -> {
            ids.add(log.getObjId());
        });
        //根据文章id集合查出出现次数最多的10个关键词
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("keywords").field("keyword_filter").size(10);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("id", ids))
                .from(1).size(0)
                .aggregation(aggregationBuilder);
        Map<String, List<? extends Terms.Bucket>> facet = documentService.getFacetByQuery(builder, ArticleDoc.class);
        List<? extends Terms.Bucket> buckets = facet.get("keywords");

        //根据查出来的关键词检索文章
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        buckets.forEach(item -> {
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("keyword", item.getKeyAsString()));
        });
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        Page pageParams = getPageParams();
        Map pageList = documentService.getPageList(nativeSearchQueryBuilder.build(), PageRequest.of((int) (pageParams.getCurrent() - 1), (int) pageParams.getSize()), ArticleDoc.class);
        Object page = pageList.get("page");
        return JsonResult.Get("pageData", page);
    }

    /**
     * 获取资源定制列表
     *
     * @param request
     * @return
     */
    @GetMapping("getCustomizeList")
    @Log(title = "获取资源定制列表")
    public JsonResult getCustomizeList(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
        LambdaQueryWrapper<ArmUserCustomize> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmUserCustomize::getUserId, user.getId())
                .orderByDesc(BaseModel::getCreateTime);
        Page page = userCustomizeService.page(getPageParams(), wrapper);
        return JsonResult.Get("pageData", page);
    }

    /**
     * 新增或修改资源定制
     *
     * @return
     */
    @PostMapping("addOrModifyCustomize")
    public JsonResult addOrModifyCustomize(ArmUserCustomize userCustomize, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
        userCustomize.setUserId(user.getId());
        userCustomize.insertOrUpdate();
        return JsonResult.Get();
    }

    @PostMapping("deleteCustomize")
    public JsonResult deleteCustomize(Long id) {
        userCustomizeService.removeById(id);
        return JsonResult.Get();
    }

// /**
//  * 根据自定义资源定制的id检索相应的文章
//  * @param id
//  * @return
//  */
// @GetMapping("viewArticles")
// public JsonResult viewArticles(Long id,String sort){
//  ArmUserCustomize userCustomize = userCustomizeService.getById(id);
//  NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//  BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//  //条件组装
//  if(StringUtils.isNotBlank(userCustomize.getKeyword())){
//   String[] split = userCustomize.getKeyword().split(";");
//   BoolQueryBuilder sub1 = new BoolQueryBuilder();
//   for (String str : split) {
//    sub1.should(QueryBuilders.matchPhraseQuery("keyword_filter",str));
//   }
//   boolQueryBuilder.must(sub1);
//  }
//
//  if(StringUtils.isNotBlank(userCustomize.getName())){
//   boolQueryBuilder.must(QueryBuilders.matchQuery("title",userCustomize.getName()));
//  }
//
//  if(StringUtils.isNotBlank(userCustomize.getArticleType())){
//   String[] split = userCustomize.getArticleType().split(";");
//   boolQueryBuilder.must(QueryBuilders.termsQuery("source_type",split));
//  }
//
//  if(userCustomize.getBeginYear() != null){
//   boolQueryBuilder.must(QueryBuilders.rangeQuery("pub_year").gte(userCustomize.getBeginYear()));
//  }
//
//  if(userCustomize.getEndYear() != null){
//   boolQueryBuilder.must(QueryBuilders.rangeQuery("pub_year").lte(userCustomize.getEndYear()));
//  }
//
//  if(userCustomize.getClassId() != null){
//   ArmClcClassInfo classInfo = clcClassInfoService.getById(userCustomize.getClassId());
//   String classCode = classInfo.getClassCode();
//   boolQueryBuilder.must(QueryBuilders.wildcardQuery("clc_no",classCode+"*"));
//  }
//
//  builder.withQuery(boolQueryBuilder);
//  if(StringUtils.isNotBlank(sort)){
//   builder.withSort(SortBuilders.fieldSort(sort).order(SortOrder.DESC));
//  }
//
//  Page pageParams = getPageParams();
//  Map result = documentService.getPageList(builder.build(), PageRequest.of((int) pageParams.getCurrent() - 1, (int) pageParams.getSize()), ArticleDoc.class);
//  Object pageData = result.get("page");
//
//  return JsonResult.Get("pageData",pageData);
// }
}
