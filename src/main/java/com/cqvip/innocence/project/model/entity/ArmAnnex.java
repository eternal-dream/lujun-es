package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;

import java.math.BigInteger;
import java.sql.Clob;

import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_ANNEX")
@ApiModel(value = "ArmAnnex对象", description = "附件表")
public class ArmAnnex extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 附件标题
     */
    private String title;
    /**
     * 附件来源
     */
    @TableField("ANNEX_SRC")
    private VipEnums.AnnexSrc annexSrc;
    /**
     * 附件路径
     */
    @TableField("FILE_PATH")
    private String filePath;
    /**
     * 附件大小（单位:kb）
     */
    @TableField("FILE_SIZE")
    private Integer fileSize;
    /**
     * 资源链接 (扩展)
     */
    private String url;
    /**
     * 资源原始ID（根据来源区分，对应到News表，NetDisk表）
     */
    @TableField("REAL_ID")
    private Long realId;
    /**
     * 视频时间长度
     */
    @TableField("TIME_LENGTH")
    private String timeLength;


}
