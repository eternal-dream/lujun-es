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
 * 新闻日志
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NEWS_LOG")
@ApiModel(value = "ArmNewsLog对象", description = "新闻日志")
public class ArmNewsLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("OPERATE_TYPE")
    private VipEnums.OperateType operateType;

    @TableField("ADMIN_USER_ID")
    private Long adminUserId;

    private String content;

    @TableField("OBJ_ID")
    private Long objId;

    @TableField("IP_ADDRESS")
    private String ipAddress;

    @TableField("ADMIN_USER_NAME")
    @ApiModelProperty("操作人账户名")
    private String adminUserName;
}
