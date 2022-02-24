package com.cqvip.innocence.framework.config.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * @ClassName MultipartConfig
 * @Description 文件上传配置
 * @Author Innocence
 * @Date 2020/9/10 17:46
 * @Version 1.0
 */
@Configuration
public class MultipartConfig {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String  maxFileSize;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String requestMaxSize;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单次请求单个数据大小
        factory.setMaxFileSize(DataSize.parse(maxFileSize)); // KB,MB
        // 单次请求总上传数据大小
        factory.setMaxRequestSize(DataSize.parse(requestMaxSize));
        return factory.createMultipartConfig();
    }
}
