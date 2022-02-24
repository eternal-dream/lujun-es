package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单管理表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_MENU")
@ApiModel(value = "ArmMenu对象", description = "菜单管理表")
public class ArmMenu extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单URL地址
     */
    private String url;
    /**
     * 菜单层级
     */
    private Integer level;
    /**
     * 父级ID,父菜单为0
     */
    @TableField("PARENT_ID")
    private Long parentId;
    /**
     * 菜单顺序
     */
    private Integer sort;
    /**
     * 创建者ID
     */
    @TableField("FOUNDER_ID")
    private Long founderId;
    /**
     * 图标
     */
    private String icon;

    @TableField("IS_DEFAULT")
    private Boolean isDefault;


}
