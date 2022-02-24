package com.cqvip.innocence.framework.filter;

import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.framework.aware.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * xss过滤，过滤所有请求
 * @author Innocence
 * @date 2020/9/21
 */
@WebFilter(urlPatterns = {"/*"})
@Component
@Slf4j
public class XssFilter implements Filter {

	private Map<String, Boolean> needFilterCache = new HashMap();


	@Autowired
	private Application application;

	private static RequestMappingHandlerMapping handlerMapping;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if(needFilter(request)){
			XssHttpServletRequestWrapper xssRequestWrapper = new XssHttpServletRequestWrapper(req);
			chain.doFilter(xssRequestWrapper, response);
			return;
		}
		chain.doFilter(request, response);
//		for (String s:excludeList) {
//			if (s.indexOf("*")!=-1){
//				String substring = s.substring(0, s.indexOf("*"));
//				if (uri.indexOf(substring)!=-1){
//					chain.doFilter(request, response);
//					return;
//				}
//			}
//		}

	}

	private boolean needFilter(ServletRequest request) {
		String uri = ((HttpServletRequest)request).getRequestURI();
		Boolean needFilter = needFilterCache.get(uri);
		if(needFilter != null){
			return needFilter;
		}
		XssExclusion	exclusion = null;
		handlerMapping = (RequestMappingHandlerMapping) application.getBean(RequestMappingHandlerMapping.class);
		HandlerExecutionChain handler = null;
		try {
			handler = handlerMapping.getHandler((HttpServletRequest) request);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取HandlerExecutionChain异常",e);
		}
		if(handler == null){
			needFilter = false;
		}else{
			Object targetHandler = handler.getHandler();
			if (!(targetHandler instanceof HandlerMethod)) {
				needFilter = false;
			} else {
				HandlerMethod handlerMethod = (HandlerMethod)targetHandler;
				exclusion = (XssExclusion)handlerMethod.getMethodAnnotation(XssExclusion.class);
				needFilter = exclusion == null;
			}
		}
		needFilterCache.put(uri,needFilter);

		return needFilter;
	}

	@Override
	public void destroy() {
	}
	/**
	 * 过滤json类型,response会被过滤，如果不过滤response，可以注释掉
	 * @param builder
	 * @return
	 */
//	@Bean
//	@Primary
//	public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
//		//解析器
//		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//		//注册xss解析器
//		SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
//		xssModule.addSerializer(new XssStringJsonSerializer());
//		objectMapper.registerModule(xssModule);
//		//返回
//		return objectMapper;
//	}
}
