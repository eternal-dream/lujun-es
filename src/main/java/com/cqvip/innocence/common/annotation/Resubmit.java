package com.cqvip.innocence.common.annotation;

import java.lang.annotation.*;

/**
 * @Author eternal
 * @Date 2021/11/10
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resubmit {

 /**
  * 延时时间 在延时多久后可以再次提交
  *
  * @return Time unit is one second
  */
 int delayMilliSeconds() default 500;
}