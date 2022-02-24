package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.sql.Clob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新刊发布内容表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NEW_JOURNAL_ARTICLE")
@ApiModel(value = "ArmNewJournalArticle对象", description = "新刊发布内容表")
public class ArmNewJournalArticle extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("JOURNAL_ID")
    private Long journalId;

    @TableField("IMG_URL")
    private String imgUrl;

    private String content;

    @TableField("VISIT_COUNT")
    private Integer visitCount;

    @TableField("DOWN_COUNT")
    private Integer downCount;

    @TableField("USER_ID")
    @ApiModelProperty("发布人ID，对应管理员")
    private Long userId;

    @TableField(exist = false)
    @ApiModelProperty("发布人姓名")
    private String userName;

    private Integer sortId;


}
