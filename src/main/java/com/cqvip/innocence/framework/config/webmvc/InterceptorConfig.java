package com.cqvip.innocence.framework.config.webmvc;

import com.cqvip.innocence.framework.interceptor.FrontUserInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @ClassName InterceptorConfig
 * @Description 注册拦截器
 * @Author Innocence
 * @Date 2021/9/26 17:07
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private FrontUserInterceptor loginInterceptor;

    @Value("${base-url.front}")
    private String baseUrl;
    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //可以多个拦截器
//       InterceptorRegistration interceptorDemoRegistration = registry.addInterceptor(new InterceptorDemo());
        //interceptorDemoRegistration.addPathPatterns("/..");
        //interceptorDemoRegistration.addPathPatterns("/..");

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(loginInterceptor);
        //可以设置多个路径拦截
        interceptorRegistration.addPathPatterns("/"+baseUrl+"/**");
//        interceptorRegistration.addPathPatterns("/**"); //拦截全部
//        interceptorRegistration.excludePathPatterns("/view/userInfo/login","/view/userInfo/logOut",
//                "/view/userInfo/regis","/view/userInfo/forgetPassword","/view/userInfo/getLoginUser",
//                "/view/news/getNotices","/view/websiteConfig/getLornDueDate");

    }
}
