package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;

import com.cqvip.innocence.project.model.enums.SearchType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资源检索日志
 * </p>
 *
 * @author Innocence
 * @since 2021-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ARTICLE_SEARCH_LOG")
@ApiModel(value = "ArmArticleSearchLog对象", description = "资源检索日志")
public class ArmArticleSearchLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private Long userId;

    @TableField("IP_ADDRESS")
    private String ipAddress;

    @TableField("SEARCH_CONDITION")
    private String searchCondition;

    @TableField("LOGIN_NAME")
    private String loginName;

    @TableField("READER_SRC")
    private SearchType.ReaderSrc readerSrc;

    @TableField("READER_TYPE")
    private String readerType;

    @TableField("READER_UNIT")
    private String readerUnit;

    @TableField("DISPLAY_INFO")
    private String displayInfo;

}
