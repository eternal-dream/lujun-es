package com.cqvip.innocence.project.model.entity;

import com.cqvip.innocence.common.annotation.DocumentId;
import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

/**
 * elasticsearch对应实体类
 * 后缀带alt的代表英文
 * @author Innocence
 * @date 2021/8/23
 */
@Data
@Document(indexName = "source_type_article_tpl")
public class ArticleDoc implements Serializable {
    private static final long serialVersionUID = 6735913456541605819L;

    @DocumentId
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(name = "title",searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    @ApiModelProperty("标题")
    private String title;

    @Field(name = "title_alt",searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    @ApiModelProperty("英文标题")
    private String titleAlt;

    @Field(name = "title_succession")
    @ApiModelProperty("暂时不清楚用法")
    private String titleSuccession;

    @Field(name = "source_type",type = FieldType.Keyword)
    @ApiModelProperty("文献类型VipEnums.ArticleType")
    private Integer sourceType;

    @Field(name = "source_type_filter",type = FieldType.Keyword)
    @ApiModelProperty("文献类型聚类数组")
    private List<Integer> sourceTypeFilter;

    @Field(name = "author")
    @ApiModelProperty("作者")
    private String author;

    @Field(name = "author_filter",type = FieldType.Keyword)
    @ApiModelProperty("作者聚类数组")
    private List<String> authorFilter;

    @Field(name = "author_alt")
    @ApiModelProperty("作者（英文）")
    private String authorAlt;

    @Field(name = "author_alt_filter" ,type = FieldType.Keyword)
    @ApiModelProperty("作者（英文）聚类数组")
    private List<String> authorAltFilter ;

    @Field(name = "keyword",searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    @ApiModelProperty("关键词")
    private String keyword;

    @Field(name = "keyword_filter",type = FieldType.Keyword)
    @ApiModelProperty("关键词聚类数组")
    private List<String> keywordFilter;

    @Field(name = "keyword_alt",analyzer = "english")
    @ApiModelProperty("英文关键词")
    private String keywordAlt;

    @Field(name = "keyword_alt_filter",type = FieldType.Keyword)
    @ApiModelProperty("英文关键词聚类数组")
    private String keywordAltFilter;

    @Field(name = "organ",searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    @ApiModelProperty("机构")
    private String organ;

    @Field(name = "organ_filter",type = FieldType.Keyword)
    @ApiModelProperty("机构聚类数组")
    private List<String> organFilter;

    @Field(name = "srcid",type = FieldType.Keyword)
    @ApiModelProperty("来源id数组,（具体使用场景和来源后面问晓菲）")
    private List<String> srcId;

    @Field(name = "provider_id",type = FieldType.Keyword)
    @ApiModelProperty("来源数据库id数组")
    private List<String> providerId ;

    @Field(name = "provider",type = FieldType.Keyword)
    @ApiModelProperty("来源数据库名称或标识数组")
    private List<String> provider;

    @Field(name = "provider_url",type = FieldType.Keyword)
    @ApiModelProperty("原链接数组")
    private List<String> providerUrl ;

    @Field(name = "journal_name",type = FieldType.Keyword)
    @ApiModelProperty("期刊名")
    private String journalName ;

    @Field(name = "publisher",type = FieldType.Keyword)
    @ApiModelProperty("出版商")
    private String publisher;

    @Field(name = "isbn")
    @ApiModelProperty("书号（索引里面没有设为keyword,如果要查询,记得加上.keyword）")
    private String isbn;

    @Field(name = "clc_no",type = FieldType.Keyword)
    @ApiModelProperty("中图分类号数组")
    private List<String> clcNo;

    @Field(name = "num",type = FieldType.Keyword)
    @ApiModelProperty("期次")
    private String num;

    @Field(name = "abstract",searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    @ApiModelProperty("摘要")
    private String summary;

    @Field(name = "abstract_alt",analyzer = "english")
    @ApiModelProperty("英文摘要")
    private String summaryEn;

    @Field(name = "language",type = FieldType.Keyword)
    @ApiModelProperty("语种")
    private String  language;

    @Field(name = "modifytime",type = FieldType.Integer)
    @ApiModelProperty("修改时间，索引里面是integer，示例数据为YYYYMMDD")
    private Integer modifyTime;

    @Field(name = "state",type = FieldType.Integer)
    @ApiModelProperty("是否屏蔽，0未屏蔽，1屏蔽")
    private Integer state;

    @Field(name = "repeat",type = FieldType.Boolean)
    @ApiModelProperty("是否重复，true为重复，表示这条数据有多个来源")
    private Boolean repeat;

    @Field(name = "similar",type = FieldType.Boolean)
    @ApiModelProperty("是否相似，原去重算法里面标识疑似数据")
    private Boolean similar;

    @Field(name = "pub_year",type = FieldType.Integer)
    @ApiModelProperty("发布年份")
    private Integer pubYear;

    @Field(name = "firstclass",type = FieldType.Keyword)
    @ApiModelProperty("第一中图分类")
    private String firstClass;

    @Field(name = "firstauthor",type = FieldType.Keyword)
    @ApiModelProperty("第一作者")
    private String firstAuthor;

    @Field(name = "full_text")
    @ApiModelProperty("全文")
    private String fullText;

    @Field(name = "fund",type = FieldType.Keyword)
    @ApiModelProperty("基金")
    private String fund;
}
