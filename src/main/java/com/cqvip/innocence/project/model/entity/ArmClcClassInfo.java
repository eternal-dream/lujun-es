package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 中图分类表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_CLC_CLASS_INFO")
@ApiModel(value = "ArmClcClassInfo对象", description = "中图分类表")
public class ArmClcClassInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("CLASS_CODE")
    private String classCode;

    @TableField("PARENT_ID")
    private Long parentId;

    @TableField("CLASS_NAME")
    private String className;

    private String code;

    private String level;

    @TableField("DOMAIN_TYPE")
    private Integer domainType;

    @TableField("DOMAIN_ID")
    private Long domainId;

    @TableField(exist = false)
    private List<ArmClcClassInfo> childList;

    /**
     * 用于前端检索聚类显示时显示的文档数量
     * @author Innocence
     * @date 2021/9/22
     */
    @TableField(exist = false)
    private Long docCount;

}
