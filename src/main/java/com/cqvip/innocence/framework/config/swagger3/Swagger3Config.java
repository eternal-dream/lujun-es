package com.cqvip.innocence.framework.config.swagger3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @ClassName Swagger2Config
 * @Description Swagger2配置
 * @Author Innocence
 * @Date 2020/7/13
 * @Version 1.0
 */
@Configuration
public class Swagger3Config {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(""))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("cqvip_innocence_api接口文档")
                .description("Swagger3")
                .version("1.0")
                .build();
    }
}
