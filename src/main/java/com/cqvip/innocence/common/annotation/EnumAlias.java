package com.cqvip.innocence.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举别名注解
 * @author 卢有佳(finemi)
 * @date 2016年8月5日 下午1:08:12
 * @version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumAlias {
	String value();
}
