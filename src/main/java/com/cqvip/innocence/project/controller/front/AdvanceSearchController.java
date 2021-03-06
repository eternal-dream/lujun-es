package com.cqvip.innocence.project.controller.front;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqvip.innocence.common.annotation.SensitiveTag;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.SearchModel;
import com.cqvip.innocence.project.model.dto.SearchParams;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.enums.SearchField;
import com.cqvip.innocence.project.service.ArmBlockWordService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.cqvip.innocence.framework.config.aspect.SensitiveAspect.SENSITIVE_KEY;

/**
 * @ClassName AdvanceSearchController
 * @Description ?????????????????????
 * @Author Innocence
 * @Date 2021/9/15 10:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/search/")
public class AdvanceSearchController {

    @Autowired
    private DocumentService<ArticleDoc> documentService;

    @Autowired
    private ArmBlockWordService blockWordService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("getFacetsToAnalyze")
    @ApiOperation("???????????????????????????????????????????????????????????????")
    public JsonResult getFacetsToAnalyze(@RequestBody SearchParams params){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<String> blockWordList = getBlockWordList();
        if (blockWordList != null && blockWordList.size() > 0) {
            String join = StringUtils.join(blockWordList.toArray(), " ");
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("whitespace"));
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("ik_max_word"));
        }
        setParams(params, boolQueryBuilder);
        TermsAggregationBuilder yearsFacet = AggregationBuilders
                .terms("pub_year")
                .field("pub_year")
                .order(BucketOrder.key(true))
                .size(20);
        TermsAggregationBuilder keywordFacet = AggregationBuilders
                .terms("keyword_filter")
                .field("keyword_filter")
                .order(BucketOrder.key(false))
                .size(40);
        TermsAggregationBuilder authorFacet = AggregationBuilders
                .terms("author_filter")
                .field("author_filter")
                .order(BucketOrder.key(false))
                .size(50);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder).aggregation(yearsFacet).aggregation(authorFacet).aggregation(keywordFacet);
        Map<String, List<? extends Terms.Bucket>> facetByQuery = null;
        try {
            facetByQuery = documentService.getFacetByQuery(searchSourceBuilder, ArticleDoc.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResult.Get().putRes(facetByQuery);
    }

    @PostMapping("getRecommonArticleList")
    @ApiOperation("???????????????????????????????????????")
    public JsonResult getRecommonArticleList(String searchExpression){
        PageRequest pageRequest = PageRequest.of(0, 10);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //???????????????
        List<String> blockWordList = getBlockWordList();
        if (blockWordList != null && blockWordList.size() > 0) {
            String join = StringUtils.join(blockWordList.toArray(), " ");
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("whitespace"));
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("ik_max_word"));
        }
        boolQueryBuilder.must(QueryBuilders.queryStringQuery(searchExpression).analyzer("ik_smart")).boost(2.0f);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .build();
        Map<String, Object> pageList = documentService.getPageList(build, pageRequest, ArticleDoc.class);
        return JsonResult.Get().putRes(pageList);
    }

    @PostMapping("getPageList")
    @ApiOperation("????????????????????????(?????????????????????????????????????????????????????????????????????????????????)")
    @SensitiveTag
    public JsonResult getPageListWithApi(@RequestBody SearchParams params, HttpServletRequest request) {
        Integer current;
        //????????????
        if (params.getPageNum() == null) {
            current = 0;
        } else {
            current = params.getPageNum() - 1;
        }
        if (params.getPageSize() == null) {
            params.setPageSize(20);
        }
        PageRequest pageRequest = PageRequest.of(current, params.getPageSize());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //???????????????
        List<String> blockWordList = getBlockWordList();
        if (blockWordList != null && blockWordList.size() > 0) {
            String join = StringUtils.join(blockWordList.toArray(), " ");
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("whitespace"));
            boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(join).analyzer("ik_max_word"));
        }
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //??????
        if (params.getSortMap() != null) {
            nativeSearchQueryBuilder = setSort(params.getSortMap());
        }
        // ??????
        List<String> list = new ArrayList<>();
        list.add("source_type");
        list.add("keyword_filter");
        list.add("pub_year");
        List<TermsAggregationBuilder> termsAggregationBuilders = addAggregation(list);
        for (TermsAggregationBuilder item : termsAggregationBuilders) {
            nativeSearchQueryBuilder.addAggregation(item);
        }
        //??????
        nativeSearchQueryBuilder.withHighlightBuilder(setHighlight());
        //???????????????
        if (params.getSearchByExpression() && StrUtil.isNotBlank(params.getSearchExpression())) {
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(params.getSearchExpression()).analyzer("ik_smart")).boost(2.0f);
        } else {
            //??????API??????
            setParams(params, boolQueryBuilder);
        }
        boolQueryBuilder.must(QueryBuilders.termQuery("state",0));
        NativeSearchQuery build = nativeSearchQueryBuilder
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .build();
        Map<String, Object> pageList = documentService.getPageList(build, pageRequest, ArticleDoc.class);
        setSearchHistory(request,params);
        return JsonResult.Get().putRes(pageList);
    }

    @GetMapping("getDetailById")
    @ApiOperation("??????ID???????????????????????????")
    public JsonResult getDetailById(String id, HttpServletRequest request) {
        ArticleDoc entityById = documentService.getEntityById(id, ArticleDoc.class);
        setVisitHistory(request, entityById);
        return JsonResult.Get().putRes(entityById);
    }

    @PostMapping("export")
    @ApiOperation("??????????????????")
    public void export(@RequestBody JSONObject object, HttpServletResponse response) throws UnsupportedEncodingException {
        List<String> ids = (List<String>)object.get("ids");
        Map<String, String> fields = (Map<String, String>)object.get("fields");
        List<String> fieldsList = new ArrayList<>();
        Iterator<String> keyIt = fields.keySet().iterator();
        while (keyIt.hasNext()){
            fieldsList.add(keyIt.next());
        }
        List<ArticleDoc> entityByIds = documentService.getEntityByIds(ids,ArticleDoc.class);
        List<Map> maps = new ArrayList<>(entityByIds.size());
        entityByIds.forEach(item->{
            Map map = JSON.parseObject(JSON.toJSONString(item), Map.class);
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                if(!fieldsList.contains(key)){
                    iterator.remove();
                }
            }
            maps.add(map);
        });
        ExcelWriter writer = ExcelUtil.getWriter();
        Iterator<Map.Entry<String, String>> iterator = fields.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            writer.addHeaderAlias(next.getKey(),next.getValue());
        }
        writer.write(maps);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String fileName = URLEncoder.encode("????????????", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(out);
    }

    /**
     * ????????????????????????
     * @author Innocence
     * @date 2021/9/15
     * @param request
     * @param articleDoc
     * @return void
     */
    private void setVisitHistory(HttpServletRequest request, ArticleDoc articleDoc) {
        ArmArticleVisitLog visitLog = new ArmArticleVisitLog();
        String ipAddr = IpUtils.getIpAddr(request);
        visitLog.setIpAddress(ipAddr);
        String id = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisUtil.get(id);
        visitLog.setLoginName(user.getLoginName());
        visitLog.setDbName(articleDoc.getProvider().size() > 0 ? articleDoc.getProvider().get(0) : "");
        visitLog.setObjId(articleDoc.getId());
        visitLog.setTitle(articleDoc.getTitle());
        visitLog.setReaderSrc(user.getReaderSrc());
        visitLog.setReaderUnit(user.getReaderUnit());
        visitLog.setUserId(user.getId());
        visitLog.insert();
    }

    /**
     * ????????????????????????????????????
     * @author Innocence
     * @date 2021/9/15 .getSearchExpression()
     * @param request
     * @param param
     * @return void
     */
    private void setSearchHistory(HttpServletRequest request, SearchParams param) {
        String showExpression = param.getShowExpression();
        String searchExpression = param.getSearchExpression();
        if (StrUtil.isNotBlank(showExpression) && StrUtil.isNotBlank(searchExpression)){
            ArmArticleSearchLog history = new ArmArticleSearchLog();
            String ipAddr = IpUtils.getIpAddr(request);
            history.setIpAddress(ipAddr);
            String id = request.getSession().getId();
            ArmUserInfo user = (ArmUserInfo) redisUtil.get(id);
            history.setLoginName(user.getLoginName());
            history.setUserId(user.getId());
            history.setReaderSrc(user.getReaderSrc());
            history.setReaderUnit(user.getReaderUnit());
            history.setSearchCondition(searchExpression);
            history.setDisplayInfo(showExpression);
            history.insert();
        }

    }

    /**
     * ???????????????
     * @author Innocence
     * @date 2021/9/8
     * @return java.util.List<java.lang.String>
     */
    private List<String> getBlockWordList() {
        List<String> oldSensitiveList = (List<String>) redisUtil.get(SENSITIVE_KEY);
        if (oldSensitiveList == null || oldSensitiveList.isEmpty()) {
            oldSensitiveList = new ArrayList<>();
            List<ArmBlockWord> list = blockWordService.list();
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    oldSensitiveList.add(list.get(i).getTitle().trim());
                }
            }
        }
        return oldSensitiveList;
    }

    /**
     * ???????????????????????????????????????
     * @author Innocence
     * @date 2021/3/23
     * @param params
     * @param boolQueryBuilder
     * @return void
     */
    @SensitiveTag
    private void setParams(SearchParams params, BoolQueryBuilder boolQueryBuilder) {
        List<String> classTypes = params.getClassTypes();
        //??????????????????
        if (classTypes != null && classTypes.size() > 0) {
            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(documentService.getFields(SearchField.CLC_NO).get(0), classTypes);
            boolQueryBuilder.must(termsQueryBuilder);
        }
        //????????????
        Map<String, List<Map<Object, Object>>> rangeMap = params.getRangeMap();
        if (!rangeMap.isEmpty()) {
            for (Map.Entry<String, List<Map<Object, Object>>> entry : rangeMap.entrySet()) {
                List<Map<Object, Object>> value = entry.getValue();
                value.forEach(item -> {
                    Object min = item.get("min");
                    Object max = item.get("max");
                    if (min != null || max != null) {
                        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(entry.getKey())
                                .from(min != null ? min : null)
                                .to(max != null ? max : null)
                                .includeLower(true)//???????????????
                                .includeUpper(true);//???????????????
                        if (item.get("logic").equals("AND")) {
                            boolQueryBuilder.must(rangeQueryBuilder);
                        } else if (item.get("logic").equals("OR")) {
                            boolQueryBuilder.should(rangeQueryBuilder);
                        } else {
                            boolQueryBuilder.mustNot(rangeQueryBuilder);
                        }
                    }
                });
            }
        }
        if (params.getAdvanceSearchParams() != null && params.getAdvanceSearchParams().size() > 0) {
            List<SearchModel> searchParams = params.getAdvanceSearchParams();
            if (searchParams.size() > 1) {
                String logic = searchParams.get(1).getLogicOperator().trim();
                if (!logic.equals("NOT")) {
                    searchParams.get(0).setLogicOperator(logic);
                }
                searchParams.forEach(item -> setAdvanceSearchParams(item, boolQueryBuilder));
            } else {
                SearchModel item = searchParams.get(0);
                List<String> fields = documentService.getFields(item.getSearchField());
                DisMaxQueryBuilder queryBuilder = QueryBuilders.disMaxQuery().tieBreaker(1.0f);
                if (item.getIsExact()) {
//                    fields.forEach(field -> queryBuilder.add(QueryBuilders.termQuery(setFieldWithFilter(field), item.getSearchKeyword()).boost(2.0f)));
                    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                    fields.forEach(field -> boolQuery.should(QueryBuilders.matchPhraseQuery(setFieldWithFilter(field), item.getSearchKeyword()).boost(2.0f)));
                    queryBuilder.add(boolQuery);
                } else {
                    queryBuilder.add(QueryBuilders.multiMatchQuery(item.getSearchKeyword()).fields(setFieldsWeight(fields))
                            .type(MultiMatchQueryBuilder.Type.MOST_FIELDS));//?????????????????????????????????????????????????????????????????????????????????????????????????????????
                }
                boolQueryBuilder.must(queryBuilder);
            }
        }
    }

    public void setAdvanceSearchParams(SearchModel item, BoolQueryBuilder boolQueryBuilder) {
        List<String> fields = documentService.getFields(item.getSearchField());
        String logic = item.getLogicOperator();
        if (StrUtil.equals("AND", logic)) {
            if (item.getIsExact()) {
                fields.forEach(field -> boolQueryBuilder.must(QueryBuilders.termQuery(setFieldWithFilter(field), item.getSearchKeyword()).boost(2.0f)));
            } else {
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                        item.getSearchKeyword()).fields(setFieldsWeight(fields)).boost(2.0f);
                boolQueryBuilder.must(multiMatchQueryBuilder);
            }
        } else if (StrUtil.equals("OR", logic)) {
            if (item.getIsExact()) {
                fields.forEach(field -> boolQueryBuilder.should(QueryBuilders.termQuery(setFieldWithFilter(field), item.getSearchKeyword()).boost(2.0f)));
            } else {
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                        item.getSearchKeyword()).fields(setFieldsWeight(fields)).boost(2.0f);
                boolQueryBuilder.should(multiMatchQueryBuilder);
            }
        } else if (StrUtil.equals("NOT", logic)) {
            if (item.getIsExact()) {
                fields.forEach(field -> boolQueryBuilder.mustNot(QueryBuilders.termQuery(setFieldWithFilter(field), item.getSearchKeyword()).boost(2.0f)));
            } else {
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                        item.getSearchKeyword()).fields(setFieldsWeight(fields)).boost(2.0f);
                boolQueryBuilder.mustNot(multiMatchQueryBuilder);
            }
        }
    }

    /**
     * ??????????????????(?????????134????????????es)???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????_filter????????????????????????
     * @author Innocence
     * @date 2021/9/24
     * @param field
     * @return java.lang.String
     */
    private String setFieldWithFilter(String field){
        String suffix = "_filter";
        if ( field.indexOf("author") !=-1 || field.indexOf("organ") != -1 || field.indexOf("keyword") != -1){
            return  field+suffix;
        }else {
            return field;
        }
    }

    /**
     * ???????????????????????????_alt???????????????????????????
     * ??????????????????es????????????????????????_alt?????????
     *
     * @param fields
     * @return java.util.Map<java.lang.String, java.lang.Float>
     * @author Innocence
     * @date 2021/9/24
     */
    private Map<String, Float> setFieldsWeight(List<String> fields) {
        Map<String, Float> map = new HashMap<>();
        fields.forEach(item -> {
            if (item.indexOf("_alt") != -1) {
                map.put(item, 1.5f);
            } else {
                map.put(item, 2.0f);
            }
        });
        return map;
    }

    /**
     * ??????????????????
     * @author Innocence
     * @date 2021/3/18
     * @param map
     * @return java.util.List<org.elasticsearch.search.sort.SortBuilder>
     */
    private NativeSearchQueryBuilder setSort(Map<String, SortOrder> map) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        for (Map.Entry<String, SortOrder> entry : map.entrySet()) {
            SortBuilder order;
            if (entry.getValue().equals(SortOrder.ASC)) {
                order = SortBuilders.fieldSort(entry.getKey()).order(SortOrder.ASC);
            } else {
                order = SortBuilders.fieldSort(entry.getKey()).order(SortOrder.DESC);
            }
            nativeSearchQueryBuilder.withSort(order);
        }
        if (map.isEmpty()){
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC));
        }
        return nativeSearchQueryBuilder;
    }

    /**
     * ?????????????????????
     * @author Innocence
     * @date 2021/3/18
     * @param fields ?????????????????????????????????????????????
     * @return java.util.List<org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder>
     */
    private List<TermsAggregationBuilder> addAggregation(List<String> fields) {
        List<TermsAggregationBuilder> builders = new ArrayList<>();
        fields.forEach(item -> {
            TermsAggregationBuilder size;
            if (item.equals("clc_no")) {
                size = AggregationBuilders.terms(item).field(item).size(600);
            } else {
                size = AggregationBuilders.terms(item).field(item).size(150);
            }
            builders.add(size);
        });
        return builders;
    }

    /**
     * ??????????????????????????????????????????
     * @author Innocence
     * @date 2021/9/15
     */
    private HighlightBuilder setHighlight(){
        String[] strings ={"title","title_alt","author","author_alt","keyword","keyword_alt","abstract","organ","journal_name"};
        HighlightBuilder highlightBuilder = documentService.getHighlightBuilder(strings);
        return highlightBuilder;
    }

}
