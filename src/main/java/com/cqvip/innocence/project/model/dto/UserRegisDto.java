package com.cqvip.innocence.project.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserRegisDto
 * @Description TODO
 * @Author Innocence
 * @Date 2021/9/14 10:17
 * @Version 1.0
 */
@Data
public class UserRegisDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "读者姓名")
    private String readerName;

    @ApiModelProperty(value = "登录账号")
    private String loginName;

    @ApiModelProperty(value = "登录密码")
    private String loginPassword;

    @ApiModelProperty(value = "性别（1：男、2：女）")
    private String sex;

    @ApiModelProperty(value = "读者单位")
    private String readerUnit;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "密保问题")
    private String ask;

    @ApiModelProperty(value = "密保问题答案")
    private String answer;
}
