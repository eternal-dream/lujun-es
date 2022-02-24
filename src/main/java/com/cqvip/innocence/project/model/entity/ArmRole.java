package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ROLE")
@ApiModel(value = "ArmRole对象", description = "")
public class ArmRole extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    @TableField(exist = false)
    private String resourceStr;

    @TableField(exist = false)
    private List<Long> resourceIds;

}
