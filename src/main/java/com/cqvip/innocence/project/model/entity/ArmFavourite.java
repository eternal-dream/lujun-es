package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户收藏表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_FAVOURITE")
@ApiModel(value = "ArmFavourite对象", description = "用户收藏表")
public class ArmFavourite extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("FAVOURITE_SRC")
    private VipEnums.FavouriteSrc favouriteSrc;

    @TableField("RESOURCE_SRC")
    private String resourceSrc;

    private String url;

    @TableField("REAL_ID")
    private String realId;

    @TableField("CLASS_ID")
    private Long classId;

    @TableField("USER_ID")
    private Long userId;

    @TableField("DB_NAME")
    private String dbName;

    @TableField(exist = false)
    @ApiModelProperty("前端传递被收藏的对象id")
    private List<String> realIds;
}
