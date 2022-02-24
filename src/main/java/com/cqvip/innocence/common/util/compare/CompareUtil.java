package com.cqvip.innocence.common.util.compare;

import com.cqvip.innocence.common.util.BaseUtil;

import java.text.DecimalFormat;

/**
 * @ClassName CpmpareUtil
 * @Description 统计用到的一些通用计算方法
 * @Author Innocence
 * @Date 2020/12/2 14:35
 * @Version 1.0
 */
public class CompareUtil extends BaseUtil {

    public static CompareUtil newInstance(){
        return (CompareUtil) BaseUtil.instance(CompareUtil.class.getName());
    }

    /**
     * 计算两数相除百分比，保留两位小数
     * @author Innocence
     * @date 2020/12/2
     * @param mole 分子
     * @param deno 分母
     * @return java.lang.String
     */
    public String percentage(Number mole,Number deno){
        DecimalFormat format = new DecimalFormat("0.00");
        double result = mole.doubleValue() / deno.doubleValue();
        return format.format(result*100)+"%";
    }
}
