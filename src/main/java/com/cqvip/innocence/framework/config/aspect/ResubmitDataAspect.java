package com.cqvip.innocence.framework.config.aspect;

import com.alibaba.fastjson.JSONObject;
import com.cqvip.innocence.common.annotation.Resubmit;
import com.cqvip.innocence.common.exception.ResubmitException;
import com.cqvip.innocence.common.lock.ResubmitLock;
import com.cqvip.innocence.project.model.dto.JsonResult;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * @ClassName RequestDataAspect
 * @Description 数据重复提交校验
 * @Author eternal
 * @Date 2021/11/09 14:25
 **/
@Log4j
@Aspect
@Component
public class ResubmitDataAspect {

 private final static String DATA = "data";
 private final static Object PRESENT = new Object();

 @Around("@annotation(com.cqvip.innocence.common.annotation.Resubmit)")
 public Object handleResubmit(ProceedingJoinPoint joinPoint) throws Throwable {
  Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
  //获取注解信息
  Resubmit annotation = method.getAnnotation(Resubmit.class);
  long delayMilliSeconds = annotation.delayMilliSeconds();
//  Object[] pointArgs = joinPoint.getArgs();
  String key = "";
  //获取第一个参数
//  Object firstParam = pointArgs[0];
  StringBuffer sb = new StringBuffer();
  ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
  String sessionId = servletRequestAttributes.getRequest().getSession().getId();
  sb.append(method.getName());
  sb.append(sessionId);
//  for (Object pointArg : pointArgs) {
//   sb.append(pointArg.toString());
//  }
  //生成加密参数 使用了content_MD5的加密方式
  key = ResubmitLock.handleKey(sb.toString());
  //执行锁
  boolean lock = false;
  try {
   //设置解锁key
   lock = ResubmitLock.getInstance().lock(key, PRESENT);
   if (lock) {
    //放行
    return joinPoint.proceed();
   } else {
    //响应重复提交异常
    throw new ResubmitException("重复提交或请求过于频繁!");
   }
  } finally {
   //设置解锁key和解锁时间
   ResubmitLock.getInstance().unLock(lock, key, delayMilliSeconds);
  }
 }
}