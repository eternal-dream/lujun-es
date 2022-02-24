package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.sql.Clob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户资源定制
 * </p>
 *
 * @author Innocence
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_USER_CUSTOMIZE")
@ApiModel(value = "ArmUserCustomize对象", description = "用户资源定制")
public class ArmUserCustomize extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String name;

    private String keyword;

    @TableField("ARTICLE_TYPE")
    private String articleType;

    @TableField(value = "BEGIN_YEAR", updateStrategy = FieldStrategy.IGNORED)
    private Integer beginYear;

    @TableField(value = "END_YEAR",updateStrategy = FieldStrategy.IGNORED)
    private Integer endYear;

    @TableField("CLASS_ID")
    private Long classId;

    @TableField("USER_ID")
    private Long userId;


}
