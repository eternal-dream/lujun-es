package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据库基础信息表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.DB_INFO")
@ApiModel(value = "DbInfo对象", description = "数据库基础信息表")
public class DbInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    private String provider;

    @TableField("ARTICLE_TYPE")
    private VipEnums.ArticleType articleType;

    private String url;

    private String remark;

    @TableField("SORT_ID")
    @ApiModelProperty("排序字段")
    private Integer sortId;

    private String supplier;

    @TableField("CLASS_ID")
    @ApiModelProperty("栏目id，对应DBinfoclass")
    private Long classId;

    @ApiModelProperty("栏目名称")
    @TableField(exist = false)
    private String classTitle;

    @TableField("IMG_URL")
    private String imgUrl;

    @TableField("TYPE_ID")
    private Long typeId;
    @TableField(exist = false)
    private String typeTitle;

    @TableField("CONTENT_ID")
    private Long contentId;
    @TableField(exist = false)
    private String contentTitle;

    @TableField("SYSTEM_ID")
    private Long systemId;
    @TableField(exist = false)
    private String systemTitle;

    @TableField("IS_MALIS")
    private Boolean isMalis;

    @TableField("IS_SEARCH")
    private Boolean isSearch;


}
