package com.cqvip.innocence.project.model.dto;

import com.cqvip.innocence.project.model.enums.SearchField;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SearchModel
 * @Description 元数据检索参数对象
 * @Author Innocence
 * @Date 2021/8/23 16:35
 * @Version 1.0
 */
@Data
public class SearchModel implements Serializable {

    /**
     * 检索字段标识符
     */
    public SearchField searchField;
    /**
     * 检索关键字
     */
    public String searchKeyword;
    /**
     * 操作逻辑运算符
     */
    public String logicOperator;
    /**
     * 展示名称
     */
    public String clusterShowName;
    /**
     * true精确/false模糊
     */
    public Boolean isExact;
}
