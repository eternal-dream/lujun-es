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
 * 网盘资源主题表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NETDISK_THEME")
@ApiModel(value = "ArmNetdiskTheme对象", description = "网盘资源主题表")
public class ArmNetdiskTheme extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("USER_ID")
    private Long userId;


}
