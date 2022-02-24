package com.cqvip.innocence.common.annotation;

import java.lang.annotation.*;

/**
 * 不需要xss过滤的方法注解
 * @Author eternal
 * @Date 2021/9/10
 * @Version 1.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssExclusion {
}