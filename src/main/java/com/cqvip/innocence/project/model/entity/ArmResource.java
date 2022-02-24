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
 * 资源表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_RESOURCE")
@ApiModel(value = "ArmResource对象", description = "资源表")
public class ArmResource extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "给前端做路由识别的路径字段")
    @TableField("MENU_URL")
    private String menuUrl;

    private String name;

    private String permission;

    private String sort;

    @ApiModelProperty(value = "图标")
    @TableField("MENU_ICON")
    private String menuIcon;


    @ApiModelProperty(value = "父菜单id")
    @TableField("PARENT_ID")
    private Long parentId;

    /** 类型（M目录 C菜单 F按钮） */
    @TableField("MENU_TYPE")
    private String menuType;

    @ApiModelProperty(value = "组件路径")
    private String component;

//    @ApiModelProperty(value = "是否是外链")
//    @TableField("IS_FRAME")
//    private Boolean isFrame;

    @ApiModelProperty(value = "是否缓存")
    @TableField("IS_CACHE")
    private Boolean isCache;

    @ApiModelProperty(value = "是否隐藏")
    @TableField("IS_HIDDEN")
    private Boolean isHidden;

}
