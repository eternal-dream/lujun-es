package com.cqvip.innocence.project.model.dto;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SearchParams
 * @Description 检索对象
 * @Author Innocence
 * @Date 2021/8/23 16:36
 * @Version 1.0
 */
@Data
public class SearchParams implements Serializable {

    /**
     * 检索条件数组
     */
    private List<SearchModel> advanceSearchParams;

    /**
     * 检索表达式
     */
    private String searchExpression;

    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * pageSize
     */
    private Integer pageSize;
    /**
     * 排序
     */
    private Map<String, SortOrder> sortMap;

    /**
     * 是否使用检索式检索（true 为使用检索式，false使用构造API）
     */
    private Boolean searchByExpression;

    /**
     * 指定查询的学科分类
     * @author Innocence
     * @date 2021/3/26
     */
    private List<String> classTypes;

    /**
     * 范围map,比如时间可能会有多个范围段，所以这里用list作为外层map的值
     * @author Innocence
     * @date 2021/9/15
     */
    private Map<String,List<Map<Object, Object>>> rangeMap;

    /**
     * 用于显示的条件
     * @author Innocence
     * @date 2021/9/27
     */
    private String showExpression;

}
