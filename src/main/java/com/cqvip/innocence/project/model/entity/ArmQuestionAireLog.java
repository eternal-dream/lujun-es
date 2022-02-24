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
 * 问卷调查日志表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_QUESTION_AIRE_LOG")
@ApiModel(value = "ArmQuestionAireLog对象", description = "问卷调查日志表")
public class ArmQuestionAireLog extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 问卷id
     */
    @TableField("QUESTION_AIRE_ID")
    private Long questionAireId;
    /**
     * 参与用户id
     */
    @TableField("USER_ID")
    private Long userId;
    /**
     * 回答内容
     */
    @TableField("OPT_CONTENT")
    private String optContent;

    private String ip;


}
