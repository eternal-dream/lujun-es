package com.cqvip.innocence.common.annotation;

import java.lang.annotation.*;

/**
 * @ClassName SensitiveTag
 * @Description 敏感词屏蔽注解
 * @Author Innocence
 * @Date 2021/1/22 11:19
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveTag {
}
