package com.cqvip.innocence.framework.interceptor;

import com.cqvip.innocence.common.exception.SensitiveException;
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
 * @ClassName SensitiveExceptionHandler
 * @Description 敏感词异常全局处理
 * @Author Innocence
 * @Date 2021/1/22 14:40
 * @Version 1.0
 */
@ControllerAdvice
public class SensitiveExceptionHandler {

    @ExceptionHandler(SensitiveException.class)
    public void sensitiveException(ServletRequest request, ServletResponse response) throws IOException {
        AxiosJudge judge = AxiosJudge.newInstance();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (judge.isAjax(httpRequest)) {
            httpResponse.sendError(JsonResult.ERROR_SENSITIVE_CODE, JsonResult.SENSITIVE_ERROR);
        }
    }
}
