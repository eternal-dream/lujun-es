package com.cqvip.innocence.common.annotation;

import java.lang.annotation.*;

/**
 * 无需登录的请求/controller
 * @Author eternal
 * @Date 2021/9/27
 * @Version 1.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLogin {

}