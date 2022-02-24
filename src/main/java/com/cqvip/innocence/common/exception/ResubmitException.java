package com.cqvip.innocence.common.exception;

/**
 * 重复提交/频繁请求 异常
 * @Author eternal
 * @Date 2021/11/10
 * @Version 1.0
 */
public class ResubmitException extends RuntimeException {
 public ResubmitException(String message){
  super(message);
 }

}