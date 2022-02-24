package com.cqvip.innocence.framework.config.cros;

/**
 * @ClassName WebMvcConfig
 * @Description 配置允许跨域调用（使用过滤器，该类废弃）
 * @Author Innocence
 * @Date 2020/8/18 13:52
 * @Version 1.0
 */
//@Configuration
public class CorsConfig {

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("*");
//        config.setAllowCredentials(true);
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);//配置CorsFilter优先级
//        return bean;
//    }

}
