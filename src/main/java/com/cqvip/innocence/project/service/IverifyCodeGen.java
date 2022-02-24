package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.dto.VerifyCode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName IverifyCodeGen
 * @Description 验证码生成接口
 * @Author Innocence
 * @Date 2020/7/28
 * @Version 1.0
 */
public interface IverifyCodeGen {

    /**
     * 生成验证码并返回code，将图片写的os中
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    String generate(int width, int height, OutputStream os) throws IOException;

    /**
     * 生成验证码对象
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    VerifyCode generate(int width, int height) throws IOException;
}
