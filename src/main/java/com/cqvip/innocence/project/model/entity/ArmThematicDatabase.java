package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题数据库
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_THEMATIC_DATABASE")
@ApiModel(value = "ArmThematicDatabase对象", description = "专题数据库")
public class ArmThematicDatabase extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String name;

    //封面图片路径
    private String url;

    private String expression;

    @TableField("IS_PUBLIC")
    private Boolean isPublic;

    @TableField("THEME_STATUS")
    private VipEnums.ThemeStatus themeStatus;

    @TableField("USER_ID")
    private Long userId;

    @TableField("THEMATIC_TYPE_ID")
    private Long thematicTypeId;

    @TableField("CLC_CLASS_ID")
    private Long clcClassId;

    private String remark;

    @TableField("BEGIN_TIME")
    private Date beginTime;

    @TableField("END_TIME")
    private Date endTime;

    private String keyword;

    /**
     * 时间：2021-09-07
     * ES字段对应source_type
     */
    @TableField("ARTICLE_TYPE")
    private String articleType;

    /**
     * 用于保存json对象的表达式用于回显
     */
    @TableField("EXP_JSON")
    private String expJson;

    @TableField(exist = false)
    private String publisher;

    @TableField(exist = false)
    private String clcClassName;

    /**
     * 纸质图书数量
     */
    @TableField(exist = false)
    private long paperBookNumber;

    /**
     * 电子图书数量
     */
    @TableField(exist = false)
    private long eBookNumber;

    /**
     * 期刊论文数量
     */
    @TableField(exist = false)
    private long journalArticleNumber;

    /**
     * 综合文献数量
     */
    @TableField(exist = false)
    private long newsNumber;


    /**
     * 视频数量
     */
    @TableField(exist = false)
    private long videoNumber;

    @TableField(exist = false)
    private List<? extends Terms.Bucket> typeAggData;
}
