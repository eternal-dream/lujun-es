package com.cqvip.innocence.framework.config.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName ShiroCrosConfig
 * @Description shiro跨域问题配置
 * @Author Innocence
 * @Date 2020/7/21 11:23
 * @Version 1.0
 */
@Configuration
public class ShiroCrosConfig extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //标识允许哪个域到请求，直接修改成请求头的域
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        //标识允许的请求方法
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        // 响应首部 Access-Control-Allow-Headers 用于 preflight request （预检请求）中，列出了将会在正式请求的
        // Access-Control-Expose-Headers 字段中出现的首部信息。修改为请求首部
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader
                ("Access-Control-Request-Headers"));
        //给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
