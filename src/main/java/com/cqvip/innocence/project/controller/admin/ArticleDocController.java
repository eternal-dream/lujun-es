package com.cqvip.innocence.project.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchParams;
import com.cqvip.innocence.project.model.entity.ArmBlockWord;
import com.cqvip.innocence.project.model.entity.ArticleDoc;
import com.cqvip.innocence.project.mongoservice.MongoService;
import com.cqvip.innocence.project.service.ArmBlockWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cqvip.innocence.framework.config.aspect.SensitiveAspect.SENSITIVE_KEY;
import static com.cqvip.innocence.project.mongoservice.impl.MongoServiceImpl.ORGIN_TITLE;

/**
 * @ClassName ArticleDocController
 * @Description es数据的管理端操作
 * @Author Innocence
 * @Date 2021/8/23 15:49
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/article/")
@Api(tags = "学术元数据的管理端操作")
public class ArticleDocController extends AbstractController {

    @Autowired
    private DocumentService<ArticleDoc> service;

    @Autowired
    private MongoService<JSONObject> mongoService;

    @Autowired
    private ArmBlockWordService blockWordService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取屏蔽词
     * @author Innocence
     * @date 2021/9/8
     * @return java.util.List<java.lang.String>
     */
    private List<String> getBlockWordList(){
        List<String> oldSensitiveList = (List<String>) redisUtil.get(SENSITIVE_KEY);
        if (oldSensitiveList == null || oldSensitiveList.isEmpty()){
            oldSensitiveList = new ArrayList<>();
            List<ArmBlockWord> list = blockWordService.list();
            if (list != null && !list.isEmpty()){
                for (int i = 0; i < list.size(); i++) {
                    oldSensitiveList.add(list.get(i).getTitle().trim());
                }
            }
        }
        return oldSensitiveList;
    }

    @PostMapping("getPageList")
    @ApiOperation("管理端获取元数据分页（前端构建检索表达式）")
    public JsonResult getArticlePageList(@RequestBody SearchParams searchParams, HttpServletRequest request){
        PageRequest pageRequest = PageRequest.of(searchParams.getPageNum()-1,searchParams.getPageSize());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String searchExpression = searchParams.getSearchExpression();
        if (StrUtil.isNotBlank(searchExpression)){
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(searchExpression)).boost(2.0f);
        }
        List<String> blockWordList = getBlockWordList();
        if (blockWordList != null && blockWordList.size() > 0){
            String join = StringUtils.join(blockWordList.toArray(), " ");
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("whitespace"));
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("ik_max_word"));
        }
//        List<SearchModel> params = searchParams.getAdvanceSearchParams();
//        if(params != null && params.size() > 0){
//            if (params.size() > 1){
//                params.forEach(item->{
//                    List<String> fields = service.getFields(item.searchField);
//                    if (fields.size() > 1){
//                        fields.forEach(f -> boolQueryBuilder.should(QueryBuilders.matchQuery(f,item.getSearchKeyword())));
//                    }else{
//                        boolQueryBuilder.must(QueryBuilders.matchQuery(fields.get(0),item.getSearchKeyword()));
//                    }
//                });
//            }else {
//                List<String> fields = service.getFields(params.get(0).searchField);
//                if (fields.size() > 1){
//                    fields.forEach(f -> boolQueryBuilder.should(QueryBuilders.matchQuery(f,params.get(0).getSearchKeyword())));
//                }else{
//                    boolQueryBuilder.must(QueryBuilders.matchQuery(fields.get(0),params.get(0).getSearchKeyword()));
//                }
//            }
//        }
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withSort(SortBuilders.fieldSort("state").order(SortOrder.ASC))
                .withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .build();
        Map<String, Object> pageList = service.getPageList(build, pageRequest,ArticleDoc.class);
        return JsonResult.Get().putPage(pageList.get("page"));
    }

    @PostMapping("updateArticle")
    @ApiOperation("更新源数据(单条数据编辑)")
    public JsonResult update(ArticleDoc articleDoc){
        service.updateByEntity(articleDoc);
        return JsonResult.Get().putRes(service.getEntityById(articleDoc.getId(),ArticleDoc.class));
    }

    @PostMapping("setStateByIds")
    @ApiOperation("根据id设置屏蔽与否，0未屏蔽，1是屏蔽")
    public JsonResult setStateByIds(@RequestBody JSONObject object){
        List<String> ids = (List<String>)object.get("ids");
        Integer state = (Integer) object.get("state");
        List<ArticleDoc> entityByIds = service.getEntityByIds(ids,ArticleDoc.class);
        entityByIds.forEach(item -> item.setState(state));
        service.updateByEntities(entityByIds);
        return JsonResult.Get();
    }

    @GetMapping("getEntityByIdForContrast")
    @ApiOperation("根据id获取实体，用作数据对比功能使用")
    public JsonResult getEntityByIdForContrast(String id){
        ArticleDoc entityById = service.getEntityById(id, ArticleDoc.class);
        List<String> srcId = entityById.getSrcId();
        List<JSONObject> objects = new ArrayList<>();
        if (srcId.size() > 0){
            srcId.forEach(item ->{
                JSONObject entity = mongoService.getEntityById(item, JSONObject.class, ORGIN_TITLE);
                objects.add(entity);
            });
        }
        JsonResult get = JsonResult.Get();
        if (objects.size() > 0){
            get.put("orgin",objects);
        }
        return get.putRes(entityById);
    }
}
