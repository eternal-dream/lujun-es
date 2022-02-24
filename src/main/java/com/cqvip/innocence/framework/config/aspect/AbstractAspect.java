package com.cqvip.innocence.framework.config.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ClassName AbstractAspect
 * @Description TODO
 * @Author Innocence
 * @Date 2021/8/3 16:40
 * @Version 1.0
 */
public abstract class AbstractAspect<T extends Annotation> {

    /**
     * 参数拼装
     */
    protected String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    if (jsonObj == null ) {
                        continue;
                    }
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    protected boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

    /**
     * 获取请求的参数
     * @author Innocence
     * @date 2021/1/22
     * @param joinPoint
     * @return java.lang.String
     */
    protected String getRequestValue(JoinPoint joinPoint){
        return argsArrayToString(joinPoint.getArgs());
    }

    /**
     * 获取方法上的注解
     * @author Innocence
     * @date 2021/1/22
     * @param joinPoint
     * @return com.cqvip.innocence.common.annotation.SensitiveTag
     */
    protected T getAnnotationTag(JoinPoint joinPoint ,Class<T> clazz) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(clazz);
        }
        return null;
    }

}
