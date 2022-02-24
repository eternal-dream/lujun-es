package com.cqvip.innocence.framework.interceptor;

import com.cqvip.innocence.common.util.axios.AxiosJudge;
import com.cqvip.innocence.project.model.dto.JsonResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName ShiroExceptionHandler
 * @Description shiro统一的异常处理
 * @Author Innocence
 * @Date 2020/9/4 15:20
 * @Version 1.0
 */
@ControllerAdvice
public class ShiroExceptionHandler {

    /**
     *  将shiro没有权限的异常统一处理，以json形式返回给axios
     * @author Innocence
     * @date 2020/9/11
     * @param request
     * @param response
     * @return void
     */
    @ExceptionHandler(UnauthorizedException.class)
    public void unAuthorization(ServletRequest request, ServletResponse response) throws IOException {
        AxiosJudge judge = AxiosJudge.newInstance();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (judge.isAjax(httpRequest)) {
            httpResponse.sendError(JsonResult.ERROR_NOAUTH_CODE,JsonResult.CODE_NOAUTH);
        }
    }

    /**
     * 未登录异常或者前端有token，但是服务器缓存被清空的情况
     * @author Innocence
     * @date 2021/1/21
     * @param request
     * @param response
     * @return void
     */
    @ExceptionHandler(AuthenticationException.class)
    public void unAuthentication(ServletRequest request, ServletResponse response) throws IOException{
        AxiosJudge judge = AxiosJudge.newInstance();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (judge.isAjax(httpRequest)) {
            httpResponse.sendError(JsonResult.ERROR_NOLOGIN_CODE,JsonResult.CODE_NOLOGIN);
        }
    }
}
