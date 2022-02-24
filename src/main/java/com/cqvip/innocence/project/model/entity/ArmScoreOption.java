package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分配置表
 * </p>
 *
 * @author Innocence
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_SCORE_OPTION")
@ApiModel(value = "ArmScoreOption对象", description = "积分配置表")
public class ArmScoreOption extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer score;

    private String description;


}
