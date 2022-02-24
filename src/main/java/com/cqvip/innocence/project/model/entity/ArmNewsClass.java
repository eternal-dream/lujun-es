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
 * 发布内容分类表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NEWS_CLASS")
@ApiModel(value = "ArmNewsClass对象", description = "发布内容分类表")
public class ArmNewsClass extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 分类名称
     */
    private String title;
    /**
     * 栏目id
     */
    @TableField("COLUMN_ID")
    private Long columnId;


}
