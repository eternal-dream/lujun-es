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
 * 资源下载日志
 * </p>
 *
 * @author Innocence
 * @since 2021-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ARTICLE_DOWN_LOG")
@ApiModel(value = "ArmArticleDownLog对象", description = "资源下载日志")
public class ArmArticleDownLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private BigInteger userId;

    @TableField("IP_ADDRESS")
    private String ipAddress;

    @TableField("OBJ_ID")
    private String objId;

    private String title;

    @TableField("DB_NAME")
    private String dbName;

    @TableField("LOGIN_NAME")
    private String loginName;

    @TableField("READER_SRC")
    private String readerSrc;

    @TableField("READER_TYPE")
    private String readerType;

    @TableField("READER_UNIT")
    private String readerUnit;


}
