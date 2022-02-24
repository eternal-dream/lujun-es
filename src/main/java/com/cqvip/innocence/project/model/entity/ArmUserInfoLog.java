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
import com.cqvip.innocence.project.model.enums.VipEnums.OperateType;

/**
 * <p>
 * 用户日志表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_USER_INFO_LOG")
@ApiModel(value = "ArmUserInfoLog对象", description = "用户日志表")
public class ArmUserInfoLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "馆员操作类型")
    @TableField("OPERATE_TYPE")
    private OperateType operateType;

    @ApiModelProperty(value = "后台管理员id")
    @TableField("ADMIN_USER_ID")
    private Long adminUserId;

    @ApiModelProperty(value = "前台用户id")
    @TableField("USER_INFO_ID")
    private Long userInfoId;

    @ApiModelProperty(value = "详细内容")
    private String content;

    @ApiModelProperty(value = "访问IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress;


}
