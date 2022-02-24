package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 问卷详细表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_QUESTION_AIRE_OPT")
@ApiModel(value = "ArmQuestionAireOpt对象", description = "问卷详细表")
public class ArmQuestionAireOpt extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("QUESTION_AIRE_ID")
    private Long questionAireId;

    private Integer type;

    @TableField("SORT_ID")
    private Long sortId;

    private String title;

    @TableField("OPT_CONTENT")
    private String optContent;


}
