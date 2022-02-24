package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.sql.Clob;
import java.util.List;

import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户网盘表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NETDISK")
@ApiModel(value = "ArmNetdisk对象", description = "用户网盘表")
public class ArmNetdisk extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("DISK_TYPE")
    private VipEnums.DiskType diskType;

    private Boolean open;

    @TableField("REQUIRE_SCORE")
    private Integer requireScore;

    private Boolean down;

    @TableField("CLASS_ID")
    private Long classId;
    @TableField(exist = false)
    private String className;

    @TableField("CREAT_USER_ID")
    private Long creatUserId;
    @TableField(exist = false)
    private String creatUserName;

    @TableField("OBJ_ID")
    private String objId;

    private String url;

    @TableField("SHARE_ARTICLE_STATUS")
    private VipEnums.ShareArticleStatus shareArticleStatus;

    private String content;

    @TableField("FILE_SIZE")
    private Integer fileSize;

    @TableField("THEME_ID")
    private Long themeId;
    @TableField(exist = false)
    private String themeName;

    private String level;

    private String source;

    private Boolean export;

    @TableField("SORT_ID")
    private Integer sortId;

    @TableField("PUB_DATE")
    private String pubDate;

    private String author;

    private String publisher;

    private Integer downTotal;

    private Integer readTotal;

    @TableField(exist = false)
    private List<ArmAnnex> annexList;
}
