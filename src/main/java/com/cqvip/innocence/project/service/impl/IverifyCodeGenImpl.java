package com.cqvip.innocence.project.service.impl;

import com.cqvip.innocence.project.model.dto.VerifyCode;
import com.cqvip.innocence.project.service.IverifyCodeGen;
import com.cqvip.innocence.common.util.iverifycode.IverifyCodeGenUtil;
import com.cqvip.innocence.common.util.random.RandomUtilsOverRide;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.cqvip.innocence.common.util.iverifycode.IverifyCodeGenUtil.VALICATE_CODE_LENGTH;


/**
 * @ClassName IverifyCodeGenImpl
 * @Description 验证码实现类
 * @Author Innocence
 * @Date 2020/7/28
 * @Version 1.0
 */
@Service
public class IverifyCodeGenImpl implements IverifyCodeGen {

    private IverifyCodeGenUtil util = IverifyCodeGenUtil.newInstance();

    /**
     * 生成验证码并返回code，将图片写的os中
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    @Override
    public String generate(int width, int height, OutputStream os) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        util.fillBackground(graphics, width, height);
        String randomStr = RandomUtilsOverRide.randomString(VALICATE_CODE_LENGTH);
        util.createCharacter(graphics, randomStr);
        graphics.dispose();
        //设置JPEG格式
        try {
            ImageIO.write(image, "JPEG", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return randomStr;
    }


    /**
     * 生成验证码对象
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    @Override
    public VerifyCode generate(int width, int height) {
        VerifyCode verifyCode ;
        try (
            //将流的初始化放到这里就不需要手动关闭流
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            String code = generate(width, height, baos);
            verifyCode = new VerifyCode();
            verifyCode.setCode(code);
            verifyCode.setImgBytes(baos.toByteArray());
        } catch (IOException e) {
            verifyCode = null;
        }
        return verifyCode;
    }
}
