package com.cqvip.innocence.project.model.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.cqvip.innocence.common.annotation.EnumAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName ArticleSearchFiled
 * @Description 文献检索条件字段
 * @Author Innocence
 * @Date 2021/8/23 16:19
 * @Version 1.0
 */
@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SearchField {

    /**
     * 任意字段
     */
    ALL,

    /**
     * 文献类型
     */
    @EnumAlias("source_type")
    SOURCE_TYPE,

    /**
     * 标题
     */
    @EnumAlias("title,title_alt")
    TITLE,

    /**
     * 关键词
     */
    @EnumAlias("keyword,keyword_alt")
    KEYWORD,

    /**
     * 作者
     */
    @EnumAlias("author,author_alt")
    AUTHOR,

    /**
     * 机构
     */
    @EnumAlias("organ")
    ORGAN,

    /**
     * 期刊分类
     */
    @EnumAlias("journal_name")
    JOURNAL_NAME,

    /**
     * 中图分类
     */
    @EnumAlias("clc_no")
    CLC_NO,

    /**
     * 来源数据库
     */
    @EnumAlias("provider")
    PROVIDER,

    /**
     * 是否重复
     */
    @EnumAlias("repeat")
    REPEAT,

    /**
     * 是否屏蔽
     */
    @EnumAlias("state")
    STATE,

    /**
     * 文摘(摘要)
     */
    @EnumAlias("summary")
    SUMMARY,

    /**
     * 第一作者
     */
    @EnumAlias("firstAuthor")
    FIRST_AUTHOR,

    /**
     * 出版方
     */
    @EnumAlias("publisher")
    PUBLISHER

}
