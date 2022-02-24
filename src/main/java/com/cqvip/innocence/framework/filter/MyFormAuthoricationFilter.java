package com.cqvip.innocence.framework.filter;

import com.cqvip.innocence.common.util.axios.AxiosJudge;
import com.cqvip.innocence.project.model.dto.JsonResult;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MyFormAuthorizationFilter
 * @Description 对于跨域的POST请求，浏览器发起POST请求前都会发送一个OPTIONS请求已确定服务器是否可用，
 *              OPTIONS请求通过后继续执行POST请求，而shiro自带的权限验证是无法处理OPTIONS请求的，
 *              所以这里需要重写isAccessAllowed方法。
 * @Author Innocence
 * @Date 2020/7/14 10:26
 * @Version 1.0
 */
@Configuration
public class MyFormAuthoricationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            return true;
        }
        return super.isAccessAllowed(servletRequest, servletResponse, o);
    }

    /**
     * 将shiro未授权，未认证的请求以json形式返回给前端ajax
     * @author Innocence
     * @date 2020/7/21
     * @param request, response
     * @return boolean
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        AxiosJudge judge = AxiosJudge.newInstance();
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            HttpServletRequest httpRequest = WebUtils.toHttp(request);
            if (judge.isAjax(httpRequest)) {
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.sendError(JsonResult.ERROR_NOLOGIN_CODE,JsonResult.CODE_NOLOGIN);
                return false;
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }

            return false;
        }
    }

}
