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
import com.cqvip.innocence.project.model.enums.VipEnums.LoginStatus;
/**
 * <p>
 * 管理用户表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ADMIN_USER")
@ApiModel(value = "ArmAdminUser对象", description = "管理用户表")
public class ArmAdminUser extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录名称")
    @TableField("LOGIN_NAME")
    private String loginName;

    @ApiModelProperty(value = "密码")
    @TableField("LOGIN_PASSWORD")
    private String loginPassword;

    @ApiModelProperty(value = "真实姓名")
    @TableField("REAL_NAME")
    private String realName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    @TableField("PHONE_NUMBER")
    private String phoneNumber;

    @ApiModelProperty(value = "登录状态")
    @TableField("LOGIN_STATUS")
    private LoginStatus loginStatus;

    @ApiModelProperty(value = "显示排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "工号")
    private String number;

    @ApiModelProperty(value = "状态")
    private VipEnums.Status status;

    @ApiModelProperty(value = "是否为全站管理员")
    @TableField("IS_ADMINISTRATOR")
    private Boolean isAdministrator;

    @TableField(exist = false)
    private String roleIds;
}
