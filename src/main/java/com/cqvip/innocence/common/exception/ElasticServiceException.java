package com.cqvip.innocence.common.exception;

/**
 * @ClassName ElasticServiceException
 * @Description 统一es操作自定义异常
 * @Author Innocence
 * @Date 2021/8/6 15:09
 * @Version 1.0
 */
public class ElasticServiceException extends RuntimeException{

    public ElasticServiceException(String msg){
        super(msg);
    }

    public ElasticServiceException(String msg,Throwable throwable){
        super(msg,throwable);
    }
}
