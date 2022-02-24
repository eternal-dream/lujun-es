package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;

import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网盘资源分类表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NETDISK_CLASS")
@ApiModel(value = "ArmNetdiskClass对象", description = "网盘资源分类表")
public class ArmNetdiskClass extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("DISK_TYPE")
    private VipEnums.DiskType diskType;

    @TableField("USER_ID")
    private Long userId;


}
