package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.cqvip.innocence.project.model.enums.VipEnums.*;
import com.cqvip.innocence.project.model.enums.SearchType.*;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_USER_INFO")
@ApiModel(value = "ArmUserInfo对象", description = "用户表")
public class ArmUserInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "借书证号(金盘条码)")
    @TableField("READER_ID")
    private String readerId;

    @ApiModelProperty(value = "读者姓名")
    @TableField("READER_NAME")
    private String readerName;

    @ApiModelProperty(value = "登录账号")
    @TableField("LOGIN_NAME")
    private String loginName;

    @ApiModelProperty(value = "登录密码")
    @TableField("LOGIN_PASSWORD")
    private String loginPassword;

    @ApiModelProperty(value = "金盘证号(一卡通id)")
    @TableField("CARD_ID")
    private String cardId;

    @ApiModelProperty(value = "读者类型")
    @TableField("READER_TYPE")
    private String readerType;

    @ApiModelProperty(value = "职称")
    @TableField("JOB_TITLE")
    private JobTitle jobTitle;

    @ApiModelProperty(value = "学位")
    private Degree degree;

    @ApiModelProperty(value = "性别（1：男、2：女）")
    private String sex;

    @ApiModelProperty(value = "借书证状态")
    @TableField("CARD_STATUS")
    private CardStatus cardStatus;

    @ApiModelProperty(value = "激活")
    @TableField("ACTIVE_STATUS")
    private ActiveStatus activeStatus;

    @ApiModelProperty(value = "账号状态")
    @TableField("REVIEW_STATUS")
    private ReviewStatus reviewStatus;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "士兵证")
    @TableField("SOLDIER_CARD")
    private String soldierCard;

    @ApiModelProperty(value = "手机号码")
    @TableField("PHONE_NUMBER")
    private String phoneNumber;

    @ApiModelProperty(value = "最近登录时间")
    @TableField("RECENT_TIME")
    private Date recentTime;

    @ApiModelProperty(value = "办证时间")
    @TableField("CERTIFICATION_TIME")
    private Date certificationTime;

    @ApiModelProperty(value = "截至日期")
    @TableField("DEADLINE_TIME")
    private Date deadlineTime;

    @ApiModelProperty(value = "离校日期")
    @TableField("LEAVE_SCHOOL_TIME")
    private Date leaveSchoolTime;

    @ApiModelProperty(value = "专家图书馆")
    @TableField("SHOW_WRITER")
    private WriterStatus showWriter;

    @ApiModelProperty(value = "介绍")
    private String present;

    @ApiModelProperty(value = "读者来源")
    @TableField("READER_SRC")
    private ReaderSrc readerSrc;

    @ApiModelProperty(value = "读者单位")
    @TableField("READER_UNIT")
    private String readerUnit;

    @ApiModelProperty(value = "研究方向")
    private String expert;

    @ApiModelProperty(value = "密保答案")
    private String answer;

    @ApiModelProperty(value = "密保问题")
    private String ask;

    @ApiModelProperty(value = "办公电话")
    private String telephone;

    @ApiModelProperty(value = "专家图书馆访问量")
    @TableField("VISIT_COUNT")
    private Integer visitCount;

    @ApiModelProperty(value = "专家认证通过时间")
    @TableField("CHECK_WRTIER_TIME")
    private Date checkWrtierTime;

    private String customize;

    @ApiModelProperty(value = "是否公开成果分析")
    @TableField("IS_ANALYSIS")
    private Boolean isAnalysis;

    @ApiModelProperty(value = "欠罚款金额")
    private BigDecimal fine;

    @ApiModelProperty(value = "管理员id")
    @TableField(value = "ADMIN_USER_ID",updateStrategy = FieldStrategy.IGNORED)
    private Long adminUserId;

    @ApiModelProperty(value = "网盘大小")
    @TableField("DISK_SIZE")
    private Integer diskSize;

    @ApiModelProperty(value = "云书包大小")
    @TableField("NET_BAG_SIZE")
    private Integer netBagSize;


}
