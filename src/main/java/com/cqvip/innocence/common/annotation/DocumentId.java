package com.cqvip.innocence.common.annotation;

import java.lang.annotation.*;

/**
 * @ClassName DocumentId
 * @Description es文档数据的id标识
 * @Author Innocence
 * @Date 2021/6/16 11:34
 * @Version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DocumentId {
}
