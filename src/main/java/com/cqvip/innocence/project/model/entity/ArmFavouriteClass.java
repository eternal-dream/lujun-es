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
 * 用户收藏分类表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_FAVOURITE_CLASS")
@ApiModel(value = "ArmFavouriteClass对象", description = "用户收藏分类表")
public class ArmFavouriteClass extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("USER_ID")
    private Long userId;


}
