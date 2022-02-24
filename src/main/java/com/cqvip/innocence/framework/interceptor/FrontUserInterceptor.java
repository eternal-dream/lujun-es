package com.cqvip.innocence.framework.interceptor;

import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.exception.FrontUserNoLoginException;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName FrontUserInterceptor
 * @Description 前台用户的登录状态拦截器
 * @Author Innocence
 * @Date 2021/9/26 16:47
 * @Version 1.0
 */
@Component
public class FrontUserInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> beanType = handlerMethod.getBeanType();
        NoLogin beanAnnotation = beanType.getAnnotation(NoLogin.class);
        NoLogin methodAnnotation = handlerMethod.getMethodAnnotation(NoLogin.class);
        if(beanAnnotation != null || methodAnnotation != null){
            return true;
        }

        String id = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(id);
        if (null == user){
            throw new FrontUserNoLoginException("前台用户登录状态已过期");
        }else{
            redisUtil.expire(id,60*30L);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {

    }

}
