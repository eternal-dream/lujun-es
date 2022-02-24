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
 * 数据库资源访问表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.DB_INFO_LOG")
@ApiModel(value = "DbInfoLog对象", description = "数据库资源访问表")
public class DbInfoLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private Long userId;

    private String content;

    @TableField("DB_ID")
    private Long dbId;


}
