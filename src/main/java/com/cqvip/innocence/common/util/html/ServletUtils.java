package com.cqvip.innocence.common.util.html;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 客户端工具类
 * 
 */
public class ServletUtils
{
    /**
     * 获取String参数
     */
    public static String getParameter(String name)
    {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue)
    {
    	String parameter=getRequest().getParameter(name);
    	if(parameter==null) {
    		return defaultValue;
    	}
        return getRequest().getParameter(name).toString();
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name)
    {
        return toInt(getRequest().getParameter(name),null);
    }

    private static Integer toInt(Object value,Integer defaultValue) {
   	 if (value == null)
     {
         return defaultValue;
     }
     if (value instanceof Integer)
     {
         return (Integer) value;
     }
     if (value instanceof Number)
     {
         return ((Number) value).intValue();
     }
     final String valueStr = toStr(value, null);
     if (StringUtils.isEmpty(valueStr))
     {
         return defaultValue;
     }
     try
     {
         return Integer.parseInt(valueStr.trim());
     }
     catch (Exception e)
     {
         return defaultValue;
     }
	}

	private static String toStr(Object value, String defaultValue) {
		if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof String)
        {
            return (String) value;
        }
        return value.toString();
	}

	/**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue)
    {
        return toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession()
    {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     * 
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1)
        {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        {
            return true;
        }

        String uri = request.getRequestURI();
        if(uri.contains(".json")||uri.contains(".xml"))
        {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if(uri.contains("json")||uri.contains("xml"))
        {
            return true;
        }
        return false;
    }
}
