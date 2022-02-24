package com.cqvip.innocence.common.sessions;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @ClassName MySessionManager
 * @Description 自定义的session获取
 * @Author Innocence
 * @Date 2020/7/14 10:31
 * @Version 1.0
 */
public class MySessionManager  extends DefaultWebSessionManager {
    /**
     * 定义请求头中使用的标记key，用来传递sessionid
     */
    private static final String AUTH_TOKEN  = "sessionId";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public MySessionManager(){
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response){
        String id = WebUtils.toHttp(request).getHeader(AUTH_TOKEN );
        //如果请求头中有AUTHORIZATION，其值为sessionid
        if (!StringUtils.isBlank(id)){
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
        //否则按默认规则从cookie取sessionId
        return super.getSessionId(request, response);

    }

}
