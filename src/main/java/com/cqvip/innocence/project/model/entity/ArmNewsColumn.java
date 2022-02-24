package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 新闻栏目表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NEWS_COLUMN")
@ApiModel(value = "ArmNewsColumn对象", description = "新闻栏目表")
public class ArmNewsColumn extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 栏目名称
     */
    private String name;

    /**
     * 父级ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 栏目层级
     */
    private Integer level;

    /**
     * 栏目顺序
     */
    @TableField("NUMERICAL_ORDER")
    private Integer numericalOrder;

    /**
     * 创建者ID
     */
    @TableField("FOUNDER_ID")
    private Long founderId;
    /**
     * 列表展示类型
     */
    @TableField("COLUMN_TYPE")
    private VipEnums.ColumnType columnType;
    /**
     * 启用状态
     */
    private VipEnums.Status status;

    /**
     * 栏目状态
     */
    @TableField("IS_DEFAULT")
    private Boolean isDefault;

    @TableField(exist = false)
    private List<ArmNewsColumn> children;

}
