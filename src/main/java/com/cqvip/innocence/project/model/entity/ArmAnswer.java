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
 * 问题回答表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ANSWER")
@ApiModel(value = "ArmAnswer对象", description = "问题回答表")
public class ArmAnswer extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 问题内容
     */
    private String content;
    /**
     * 问题id
     */
    @TableField("QUESTION_ID")
    private Long questionId;
    /**
     * 管理员id
     */
    @TableField("ADMIN_ID")
    private Long adminId;


}
