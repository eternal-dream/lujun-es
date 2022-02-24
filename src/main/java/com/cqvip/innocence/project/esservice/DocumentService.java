package com.cqvip.innocence.project.esservice;

import com.cqvip.innocence.project.model.enums.SearchField;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.Query;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DocumentService
 * @Description 适用于本项目的数据的增删改查
 * @Author Innocence
 * @Date 2021/3/1 15:43
 * @Version 1.0
 */
public interface DocumentService<T> {

    /**
     * 设置高亮字段
     * @author Innocence
     * @date 2021/3/2
     * @param fields 需要设置的高亮字段
     * @return org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder
     */
    HighlightBuilder getHighlightBuilder(String[] fields);

    /**
     * 根据id判断文档是否存在于指定索引中
     * @author Innocence
     * @date 2021/3/1
     * @param id
     * @param clazz
     * @return java.lang.Boolean
     */
    Boolean isExist(String id, Class<T> clazz);

    /**
     *  单条数据插入
     * @author Innocence
     * @date 2021/3/1
     * @param t 需插入的数据
     * @return java.lang.String 返回文档id
     */
    String saveByEntity(T t);

    /**
     * 批量插入
     * @author Innocence
     * @date 2021/3/1
     * @param entities 待插入的数据实体集合
     * @return java.util.List<java.lang.String> 返回idList
     */
    List<String> saveBatchByEntities(List<T> entities) throws Exception;

    /**
     * 单条数据更新
     * @author Innocence
     * @date 2021/3/1
     * @param t 需要更新的实体数据
     * @return void
     */
    void updateByEntity(T t);

    /**
     * 批量更新
     * @author Innocence
     * @date 2021/3/1
     * @param entities 待更新的数据实体集合
     * @return void
     */
    void updateByEntities(List<T> entities);

    /**
     * 根据id删除数据
     * @author Innocence
     * @date 2021/3/1
     * @param id
     * @param clazz
     * @return java.lang.String 被删除的id
     */
    String deleteById(String id, Class<T> clazz);

    /**
     * 根据id批量删除数据
     * @author Innocence
     * @date 2021/3/12
     * @param ids
     * @param clazz
     * @return void
     */
    void deleteByIds(List<String> ids, Class<T> clazz);

    /**
     * 根据条件删除数据
     * @author Innocence
     * @date 2021/3/1
     * @param query 条件构造器
     * @param clazz
     * @return void
     */
    void deleteByQuery(Query query, Class<T> clazz);

    /**
     * 根据id查询数据 (基于注解形式设置了索引mapping)
     * @author Innocence
     * @date 2021/3/1
     * @param clazz
     * @return T
     */
     T getEntityById(String id,Class<T> clazz);

     /**
      * 根据id批量查询数据 (基于注解形式设置了索引mapping)
      * @author Innocence
      * @date 2021/8/24
      * @return java.util.List<T>
      */
     List<T> getEntityByIds(List<String> ids, Class<T> clazz);

    /**
     * 查询符合条件的总条数
     * @author Innocence
     * @date 2021/3/2
     * @return java.lang.Long
     */
    Long getCount(Query query,Class<T> clazz);

    /**
     * 查询符合条件的实体list
     * @author Innocence
     * @date 2021/3/2
     * @param query 构建的查询条件 主要使用NativeSearchQuery 来进行构造
     * @param isHighLight 是否映射高亮（true映射高亮，否则不映射）
     * @return java.util.List<T>
     */
    List<T> getInfoList(Query query, Class<T> clazz,Boolean isHighLight);

     /**
      * 查询符合条件的分页
      * @author Innocence
      * @date 2021/3/2
      * @param query 构建的查询条件 主要使用NativeSearchQuery 来进行构造
      * @return org.springframework.data.domain.Page<T>
      */
     Map<String,Object> getPageList(Query query, PageRequest pageRequest,Class<T> clazz);

     /**
      * 根据条件获取聚类信息
      * springboot-data 封装的聚类查询速度很慢，这里直接用client操作
      * @author Innocence
      * @date 2021/3/19
      * @param query, clazz
      * @return java.util.Map<java.lang.String,java.util.List<? extends org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket>>
      */
     Map<String, List<? extends Terms.Bucket>> getFacetByQuery(SearchSourceBuilder query, Class<T> clazz) throws IOException;

    /**
     * 根据检索对象和缩略词获取字段
     * 只能在使用了@EnumAlias的枚举为参数的情况下使用
     * @author Innocence
     * @date 2021/8/24
     * @param filed
     * @return java.util.List<java.lang.String>
     */
     List<String> getFields(SearchField filed);
}
