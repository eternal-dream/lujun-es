package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网站风格管理表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_WEBSITE_CONFIG")
@ApiModel(value = "ArmWebsiteConfig对象", description = "网站风格管理表")
public class ArmWebsiteConfig extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 站点名称
     */
    private String name;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 联系电话
     */
    @TableField("PHONE_NUMBER")
    private String phoneNumber;
    /**
     * 风格样式
     */
    private String style;
    /**
     * 站点状态
     */
    private Boolean enabled;
    /**
     * 允许附件扩展名（为空代表不限制）
     */
    @TableField("FILE_EXTENSION")
    private String fileExtension;
    /**
     * 允许附件大小（0代表不限制,单位MB）
     */
    @TableField("FILE_SIZE")
    private Integer fileSize;
    /**
     * 前台LOGO
     */
    @TableField("WEB_LOGO")
    private String webLogo;
    /**
     * 后台LOGO
     */
    @TableField("BACK_LOGO")
    private String backLogo;
    /**
     * 是否审核共享文献
     */
    @TableField("CHECK_SHARE_ARTICLE")
    private Boolean checkShareArticle;
    /**
     * 是否开启用户审核
     */
    @TableField("CHECK_USER")
    private Boolean checkUser;
    /**
     * 联系我们
     */
    @TableField("CONTACT_US")
    private String contactUs;
    /**
     * 外借天数
     */
    @TableField("LENDING_DAYS")
    private Integer lendingDays;
    /**
     * 云书包默认容量（GB）
     */
    @TableField("DISK_SIZE")
    private Integer diskSize;
    /**
     * 网盘默认容量（GB）
     */
    @TableField("NET_BAG_SIZE")
    private Integer netBagSize;

    /**
     * 微信公共号
     */
    @TableField("WX_LOGO")
    private String wxLogo;
}
