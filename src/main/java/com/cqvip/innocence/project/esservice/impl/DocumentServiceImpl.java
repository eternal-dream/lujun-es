package com.cqvip.innocence.project.esservice.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cqvip.innocence.common.annotation.DocumentId;
import com.cqvip.innocence.common.exception.ElasticServiceException;
import com.cqvip.innocence.common.util.enums.EnumUtil;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.esservice.IndexService;
import com.cqvip.innocence.project.model.enums.SearchField;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName DocumentServiceImpl
 * @Description TODO
 * @Author Innocence
 * @Date 2021/3/1 16:27
 * @Version 1.0
 */
@Service
public class DocumentServiceImpl<T> implements DocumentService<T> {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Qualifier("highLevelClient")
    @Autowired
    private RestHighLevelClient highLevelClient;

    @Autowired
    private IndexService<T> indexService;

    /**
     * 反射获取es实体代表id的字段名
     * 此方法必须在实体字段名和索引里面的名称保持一致的情况下使用
     * @author Innocence
     * @date 2021/6/17
     * @param clazz
     * @return java.lang.String
     */
    private String getPrimaryNameByClass(Class<T> clazz){
        Field[] fields = clazz.getDeclaredFields();
        for (Field f:fields) {
            DocumentId id = f.getAnnotation(DocumentId.class);
            if (id != null){
                return f.getName();
            }
        }
        return null;
    }

    /**
     * 根据注解获取实体类关联es属性的id值
     * @author Innocence
     * @date 2021/6/16
     * @return java.lang.String
     */
    private String getPrimaryValueByEntity(T t){
        Class<T> clazz = (Class<T>) t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f:fields) {
            Class<?> type = f.getType();
            if (type.getTypeName().equals(String.class.getTypeName())){
                DocumentId id = f.getAnnotation(DocumentId.class);
                if (id != null){
                    try {
                        String replace = f.getName()
                                .replace(f.getName().substring(0, 1), f.getName().substring(0, 1).toUpperCase());
                        Method method = clazz.getMethod("get" + replace);
                        String invoke = (String) method.invoke(t);
                        if (StrUtil.isNotBlank(invoke)){
                            return invoke;
                        }else {
                            return null;
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 类上无索引名的统一异常处理
     * @author Innocence
     * @date 2021/8/6
     * @param clazz
     * @return void
     */
    private String indexNameExceptionHandler(Class<T> clazz){
        String indexName = indexService.getIndexName(clazz);
        if (StrUtil.isBlank(indexName)){
            throw new ElasticServiceException("The index name on the current class does not exist！");
        }else {
            return indexName;
        }
    }

    /**
     * 映射高亮字段到原生属性
     * @author Innocence
     * @date 2021/3/2
     * @param searchHits
     * @return java.util.List<T>
     */
    private <T> List<T> mappingHighlight(List<SearchHit<T>> searchHits){
        List<T> infoList = new ArrayList<>();
        for (SearchHit<T> searchHit : searchHits) {
            T content = searchHit.getContent();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            for (Map.Entry<String, List<String>> entry : highlightFields.entrySet()) {
                try {
                    PropertyUtils.setProperty(content,entry.getKey(),entry.getValue().get(0));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            infoList.add(content);
        }
        return infoList;
    }

    @Override
    public HighlightBuilder getHighlightBuilder(String[] fields) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String field : fields) {
            highlightBuilder.field(field);
        }
        highlightBuilder.requireFieldMatch(false);     //如果要多个字段高亮,这项要为false
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段

        return highlightBuilder;
    }


    @Override
    public Boolean isExist(String id,Class<T> clazz) {
        String name = indexNameExceptionHandler(clazz);
        return restTemplate.exists(id, IndexCoordinates.of(name));
    }

    @Override
    public  String saveByEntity(T t) {
        String id = getPrimaryValueByEntity(t);
        String indexName = indexNameExceptionHandler((Class<T>) t.getClass());
        if (StrUtil.isBlank(id)){
            throw new ElasticServiceException("Document id cannot be empty！");
        }
        IndexQuery build = new IndexQueryBuilder().withId(id).withObject(t).build();
        String index = restTemplate.index(build, IndexCoordinates.of(indexName));
        //业务需要，新增后马上刷新，ElasticsearchRestTemplate是默认不立即刷新（立即刷新会影响性能）
        restTemplate.indexOps(t.getClass()).refresh();
        return index;
    }

    @Override
    public List<String> saveBatchByEntities(List<T> entities){
        Class<T> clazz =(Class<T>) entities.get(0).getClass();
        String indexName = indexNameExceptionHandler(clazz);
        List<IndexQuery> queryList = new ArrayList<>();
        for (T item:entities){
            String id = getPrimaryValueByEntity(item);
            if (StrUtil.isBlank(id)){
                throw new ElasticServiceException("Document id cannot be empty！");
            }
            IndexQuery build = new IndexQueryBuilder().withId(id).withObject(item).build();
            queryList.add(build);
        }
        List<String> idList = restTemplate.bulkIndex(queryList, IndexCoordinates.of(indexName));
        restTemplate.indexOps(IndexCoordinates.of(indexName)).refresh();
        return idList;
    }

    @Override
    public void updateByEntity(T t) {
        String indexName = indexNameExceptionHandler((Class<T>) t.getClass());
        Document document = Document.parse(JSON.toJSONString(t));
        document.setId(getPrimaryValueByEntity(t));
        UpdateQuery build = UpdateQuery.builder(document.getId())
                .withRefresh(UpdateQuery.Refresh.Wait_For) //更新后立即刷新可见（影响性能，按需注释或使用）
                .withDocument(document)
                .build();
        restTemplate.update(build, IndexCoordinates.of(indexName));
    }

    @Override
    public void updateByEntities(List<T> entities) {
        Class<T> clazz =(Class<T>) entities.get(0).getClass();
        String indexName = indexNameExceptionHandler(clazz);
        List<UpdateQuery> updateQueries = new ArrayList<>();
        entities.forEach(item->{
            Document document = Document.parse(JSON.toJSONString(item));
            document.setId(getPrimaryValueByEntity(item));
            UpdateQuery build = UpdateQuery.builder(document.getId())
//                    .withRefresh(UpdateQuery.Refresh.Wait_For)
//                    .withDocAsUpsert(true) //不加默认false。true表示更新时不存在就插入
                    .withDocument(document)
                    .build();
            updateQueries.add(build);
        });
        restTemplate.bulkUpdate(updateQueries,IndexCoordinates.of(indexName));
    }

    @Override
    public String deleteById(String id, Class<T> clazz) {
        String indexName = indexNameExceptionHandler(clazz);
        return restTemplate.delete(id,IndexCoordinates.of(indexName));
    }

    @Override
    public void deleteByIds(List<String> ids, Class<T> clazz) {
        String indexName = indexNameExceptionHandler(clazz);
        StringQuery query = new StringQuery(QueryBuilders.termsQuery(getPrimaryNameByClass(clazz), ids).toString());
        restTemplate.delete(query,clazz,IndexCoordinates.of(indexName));
    }

    @Override
    public void deleteByQuery(Query query,Class<T> clazz) {
        String indexName = indexNameExceptionHandler(clazz);
        restTemplate.delete(query,clazz,IndexCoordinates.of(indexName));
    }

    @Override
    public T getEntityById(String id,Class<T> clazz) {
        return restTemplate.get(id,clazz);
    }

    @Override
    public List<T> getEntityByIds(List<String> ids, Class<T> clazz){
        String indexName = indexNameExceptionHandler(clazz);
        NativeSearchQuery build = new NativeSearchQueryBuilder().withIds(ids).build();
        return restTemplate.multiGet(build, clazz, IndexCoordinates.of(indexName));
    }

    @Override
    public Long getCount(Query query,Class<T> clazz) {
        return restTemplate.count(query,clazz);
    }

    @Override
    public List<T> getInfoList(Query query,Class<T> clazz,Boolean isHighLight) {
        query.setTrackTotalHits(true);
        SearchHits<T> search = restTemplate.search(query,clazz);
        if (isHighLight){
            return mappingHighlight(search.getSearchHits());
        }
        List<T> infoList = new ArrayList<>();
        for (SearchHit<T> searchHit : search){
            infoList.add(searchHit.getContent());
        }
        return infoList;
    }

    @Override
    public Map<String, Object> getPageList(Query query, PageRequest pageRequest,Class<T> clazz) {
        query.setTrackTotalHits(true);
        query.setPageable(pageRequest);
        SearchHits<T> search = restTemplate.search(query,clazz);
        Aggregations aggregations = search.getAggregations();
        List<SearchHit<T>> searchHits = search.getSearchHits();
        List<T> esSourceInfos = mappingHighlight(searchHits);
        Page infos = new PageImpl(
                esSourceInfos,
                pageRequest,
                search.getTotalHits());
        Map<String, Object> map = new HashMap<>();
        map.put("page",infos);
        map.put("ag",formatFacet(aggregations));
        return map;
    }

    @Override
    public Map<String, List<? extends Terms.Bucket>> getFacetByQuery(SearchSourceBuilder query, Class<T> clazz) throws IOException {
        String indexName = indexNameExceptionHandler(clazz);
        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder builder = query;
        request.source(builder);
        SearchResponse response = highLevelClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        return formatFacet(aggregations);
    }

    /**
     * 格式化聚类数据
     * @author Innocence
     * @date 2021/3/18
     * @param aggregations
     * @return java.util.Map<java.lang.String,java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    private Map<String, List<? extends Terms.Bucket>> formatFacet(Aggregations aggregations){
        if (aggregations == null){
            return null;
        }
        Map<String, List<? extends Terms.Bucket>> map = new HashMap<>();
        List<Aggregation> list = aggregations.asList();
        list.forEach(item->{
            ParsedTerms newItem = (ParsedTerms) item;
            String name = newItem.getName();
            List<? extends Terms.Bucket> buckets = newItem.getBuckets();
            map.put(name,buckets);
        });
        return map;
    }

    @Override
    public List<String> getFields(SearchField filed) {
        List<String> fields = new ArrayList<>();
        String alias = EnumUtil.getAlias(filed);
        if (alias.indexOf(",")!=-1){
            String[] split = alias.split(",");
            for (String s : split) {
                fields.add(s);
            }
        }else {
            fields.add(alias);
        }
        return fields;
    }
}
