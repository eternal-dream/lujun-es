package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;

import java.io.Serializable;
import java.math.BigInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色权限关联表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ROLE_RESOURCE")
@ApiModel(value = "ArmRoleResource对象", description = "角色权限关联表")
public class ArmRoleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ROLE_ID")
    @TableId(type = IdType.INPUT)
    private Long roleId;

    @TableField("RESOURCE_ID")
    private Long resourceId;


}
