package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 管理员-角色关联表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ADMIN_USER_ROLE")
@ApiModel(value = "ArmAdminUserRole对象", description = "管理员-角色关联表")
public class ArmAdminUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ADMIN_USER_ID")
    private long adminUserId;

    @TableField("ROLE_ID")
    private long roleId;


}
