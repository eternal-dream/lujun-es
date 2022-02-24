package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;

import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户积分记录
 * </p>
 *
 * @author Innocence
 * @since 2021-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_USER_SCORE_LOG")
@ApiModel(value = "ArmUserScoreLog对象", description = "用户积分记录")
public class ArmUserScoreLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private Long userId;

    private Integer score;

    @TableField("OPERATE_TYPE")
    private VipEnums.ScoreType operateType;

    @TableField("OBJ_ID")
    private String objId;


}
