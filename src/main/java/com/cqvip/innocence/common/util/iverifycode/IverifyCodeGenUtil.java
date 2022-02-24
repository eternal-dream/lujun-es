package com.cqvip.innocence.common.util.iverifycode;

import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.BaseUtil;
import com.cqvip.innocence.common.util.random.RandomUtilsOverRide;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName IverifyCodeGenUtil
 * @Description 验证码工具类
 * @Author Innocence
 * @Date 2020/7/28
 * @Version 1.0
 */
public class IverifyCodeGenUtil {
    public static IverifyCodeGenUtil newInstance() {
        return (IverifyCodeGenUtil)BaseUtil.instance(IverifyCodeGenUtil.class.getName());
    }


    public static final String[] FONT_TYPES = { "\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66" };

    public static final int VALICATE_CODE_LENGTH = 4;

    /**
     * 设置背景颜色及大小，干扰线
     *
     * @param graphics
     * @param width
     * @param height
     */
    public void fillBackground(Graphics graphics, int width, int height) {
        // 填充背景
        graphics.setColor(Color.WHITE);
        //设置矩形坐标x y 为0
        graphics.fillRect(0, 0, width, height);

        // 加入干扰线条
        for (int i = 0; i < 8; i++) {
            //设置随机颜色算法参数
            graphics.setColor(RandomUtilsOverRide.randomColor(40, 150));
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.drawLine(x, y, x1, y1);
        }
    }

    /**
     * 设置字符颜色大小
     * @param g
     * @param randomStr
     */
    public void createCharacter(Graphics g, String randomStr) {
        char[] charArray = randomStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            //设置RGB颜色算法参数
            g.setColor(new Color(50 + RandomUtilsOverRide.nextInt(100),
                    50 + RandomUtilsOverRide.nextInt(100), 50 + RandomUtilsOverRide.nextInt(100)));
            //设置字体大小，类型
            g.setFont(new Font(FONT_TYPES[RandomUtilsOverRide.nextInt(FONT_TYPES.length)], Font.BOLD, 30));
            //设置x y 坐标
            g.drawString(String.valueOf(charArray[i]), 25 * i + 5, 25 + RandomUtilsOverRide.nextInt(8));
        }
    }

    /**
     * 验证码校验
     * @param session
     * @param code
     * @return
     */
    public String checkCode(HttpSession session,String code){
        String createdCode = (String) session.getAttribute(SessionKeys.VcodeKeys.CODE);
        if (StringUtils.isBlank(createdCode)){
            return "验证码不能为空！";
        } else if (!code.equalsIgnoreCase(createdCode)){
            return "验证码不正确！";
        } else {
            Date startTime = (Date) session.getAttribute( SessionKeys.VcodeKeys.CREAT_TIME);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date nowTime=null;
            try {
                nowTime= format.parse(format.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (nowTime.getTime()-startTime.getTime()>= SessionKeys.VcodeKeys.EXPIRETION_TIME){
                return "验证码超时，请重新获取！";
            }

        }
        return null;
    }
}
