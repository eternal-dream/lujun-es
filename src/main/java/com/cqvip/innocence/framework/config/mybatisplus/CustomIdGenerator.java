package com.cqvip.innocence.framework.config.mybatisplus;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * @ClassName CustomIdGenerator
 * @Description mybatis-plus 自定义id生成器，默认使用雪花算法+UUID(不含中划线)
 *                内置支持：DB2KeyGenerator、H2KeyGenerator、KingbaseKeyGenerator、
 *                          OracleKeyGenerator、PostgreKeyGenerator
 *                当内置支持不满足需求时，可以用下面的方法自定义主键生成策略（配置后打开Component注解）
*                 IdentifierGenerator 里面有两个方法，选择一个实现即可
 * @Author Innocence
 * @Date 2020/7/11
 * @Version 1.0
 */
//@Component
public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        /**
         * todo 要实现的主键生成策略
         */
        return null;
    }
}
