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
 * 登录日志表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_LOGIN_LOG")
@ApiModel(value = "ArmLoginLog对象", description = "登录日志表")
public class ArmLoginLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private Long userId;

    @TableField("IP_ADDRESS")
    private String ipAddress;

    @ApiModelProperty(value = "登录状态（true成功，false失败）")
    private Boolean status;


}
