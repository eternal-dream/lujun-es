package com.cqvip.innocence.common.exception;

/**
 * @ClassName SensitiveException
 * @Description 敏感字异常
 * @Author Innocence
 * @Date 2021/1/22 14:33
 * @Version 1.0
 */
public class SensitiveException extends RuntimeException {

    public SensitiveException(String msg){
        super(msg);
    }
}
