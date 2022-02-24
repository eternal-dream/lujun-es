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
 * 资源分类信息
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.DB_INFO_RESOURCE")
@ApiModel(value = "DbInfoResource对象", description = "资源分类信息")
public class DbInfoResource extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    private VipEnums.ResourceType type;

    @TableField("SORT_ID")
    private Integer sortId;


}
