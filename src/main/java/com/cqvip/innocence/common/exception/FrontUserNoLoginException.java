package com.cqvip.innocence.common.exception;

/**
 * @ClassName FrontUserNoLoginException
 * @Description 前台用户未登录异常
 * @Author Innocence
 * @Date 2021/9/26 16:54
 * @Version 1.0
 */
public class FrontUserNoLoginException extends RuntimeException{

    public FrontUserNoLoginException(String msg){
        super(msg);
    }

    public FrontUserNoLoginException(String msg,Throwable throwable){
        super(msg,throwable);
    }

}
