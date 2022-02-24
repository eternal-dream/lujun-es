package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;

import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 管理员操作记录表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ADMIN_USER_LOG")
@ApiModel(value = "ArmAdminUserLog对象", description = "管理员操作记录表")
public class ArmAdminUserLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("OPERATE_TYPE")
    private VipEnums.AdminUserLogType operateType;

    @ApiModelProperty(value = "操作人ID")
    @TableField("OPERAT_AT")
    private Long operatAt;

    @ApiModelProperty(value = "操作人用户名")
    @TableField("OPERAT_NAME")
    private String operatName;

    @ApiModelProperty(value = "被操作的数据id")
    @TableField("OBJ_ID")
    private Long objId;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress;


}
