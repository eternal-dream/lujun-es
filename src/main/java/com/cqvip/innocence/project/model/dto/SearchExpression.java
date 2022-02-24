package com.cqvip.innocence.project.model.dto;

import com.cqvip.innocence.project.model.enums.SearchField;
import com.cqvip.innocence.project.model.enums.SearchSeparator;
import lombok.Data;

/**
 * @Author eternal
 * @Date 2021/9/15
 * @Version 1.0
 */
@Data
public class SearchExpression {

 private SearchSeparator separator;

 private SearchField key;

 private String value;

 //精确/模糊
 private String type;
}