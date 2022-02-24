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
 * 专题库类型表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_THEMATIC_TYPES")
@ApiModel(value = "ArmThematicTypes对象", description = "专题库类型表")
public class ArmThematicTypes extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String name;

    @TableField("PARENT_ID")
    private Long parentId;

    private Integer level;

    private Integer sort;

    @TableField("USER_ID")
    private Long userId;

    @TableField(exist = false)
    private String publisher;


}
