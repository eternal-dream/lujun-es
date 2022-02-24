package com.cqvip.innocence.framework.interceptor;

import com.cqvip.innocence.common.exception.FrontUserNoLoginException;
import com.cqvip.innocence.common.exception.ResubmitException;
import com.cqvip.innocence.common.util.axios.AxiosJudge;
import com.cqvip.innocence.project.model.dto.JsonResult;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName FrontUserNoLoginExceptionHandler
 * @Description 前台用户登录状态过期异常统一捕获
 * @Author Innocence
 * @Date 2021/9/26 16:55
 * @Version 1.0
 */
@ControllerAdvice
public class FrontUserNoLoginExceptionHandler {

    @ExceptionHandler(FrontUserNoLoginException.class)
    public void frontUserNoLoginException(ServletRequest request, ServletResponse response) throws IOException {
        AxiosJudge judge = AxiosJudge.newInstance();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (judge.isAjax(httpRequest)) {
            httpResponse.sendError(4011, JsonResult.CODE_TIMEOUT);
        }
    }

    @ExceptionHandler(ResubmitException.class)
    public void ResubmitException(ServletRequest request, ServletResponse response) throws IOException {
        AxiosJudge judge = AxiosJudge.newInstance();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (judge.isAjax(httpRequest)) {
            httpResponse.sendError(666, JsonResult.CODE_TIMEOUT);
        }
    }

}
