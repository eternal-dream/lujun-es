package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 新闻表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NEWS")
@ApiModel(value = "ArmNews对象", description = "新闻表")
public class ArmNews extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 栏目ID
     */
    @TableField("COLUMN_ID")
    private Long columnId;

    /**
     * 新闻标题
     */
    private String title;

    ///**
    // * 发布时间
    // */
    //@TableField("RELEASE_TIME")
    //private Date releaseTime;

    /**
     * 发布人
     */
    @TableField("PUBLISH_AUTHOR")
    private Long publishAuthor;

    /**
     * 发布内容
     */
    @TableField("PUBLISH_CONTENT")
    private String publishContent;

    /**
     * 链接地址
     */
    @TableField("LINK_ADDRESS")
    private String linkAddress;

    /**
     * 封面链接
     */
    @TableField(value="IMG_URL",updateStrategy = FieldStrategy.IGNORED,whereStrategy =FieldStrategy.IGNORED )
    private String imgUrl;

    /**
     * 审核状态
     */
    @TableField("AUDIT_STATUS")
    private VipEnums.AuditStatus auditStatus;

    /**
     * 排序字段
     */
    private Integer numericalOrder;
    /**
     * 分类id
     */
    @TableField("CLASS_ID")
    private Long classId;

    /**
     * 下载量
     */
    @TableField("DOWN_COUNT")
    private Integer downCount;

    /**
     * 访问量
     */
    @TableField("VISIT_COUNT")
    private Integer visitCount;

    /**
     * 制作者
     */
    private String producer;

    /**
     * 主讲人
     */
    @ApiModelProperty(value = "主讲人")
    private String speaker;

    /**
     * 专题名称
     */
    @TableField("THEME_NAME")
    private String themeName;

    /**
     * 视频来源
     */
    @TableField("VIDEO_SRC")
    private String videoSRC;

    @TableField(exist = false)
    private String publishAuthorName;

    @TableField(exist = false)
    private String columnName;
    /**
     * 附件列表
     */
    @TableField(exist = false)
    private List<ArmAnnex> armAnnexList;

    /**
     * 对应类型列表
     */
    @TableField(exist = false)
    private List<ArmNewsClass> armNewsClassList;
}
