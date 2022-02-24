package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据库资源更新日志表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.DB_INFO_UPDATE_LOG")
@ApiModel(value = "DbInfoUpdateLog对象", description = "数据库资源更新日志表")
public class DbInfoUpdateLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private Long userId;

    @TableField(exist = false)
    private String realName;

    private String content;

    @TableField("UPDATE_COUNT")
    private Long updateCount;

    @TableField("UPDATE_SIZE")
    private Long updateSize;

    @TableField("DB_ID")
    private Long dbId;

    @TableField(exist = false)
    private String title;

}
