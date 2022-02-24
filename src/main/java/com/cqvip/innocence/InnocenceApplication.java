package com.cqvip.innocence;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Innocence
 */
@SpringBootApplication
@MapperScan("com.cqvip.innocence.project.**.mapper")//配置后可以不用在Mapper接口上写@Mapper注解
@EnableOpenApi
@EnableTransactionManagement
public class InnocenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnocenceApplication.class, args);
    }

}
