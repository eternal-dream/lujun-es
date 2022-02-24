package com.cqvip.innocence.project.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName VerifyCode
 * @Description 验证码
 * @Author Innocence
 * @Date 2020/7/28
 * @Version 1.0
 */
@Data
public class VerifyCode implements Serializable {
    private static final long serialVersionUID = 3289411973088151100L;
    private String code;
    private byte[] imgBytes;
    private long expireTime;
}
