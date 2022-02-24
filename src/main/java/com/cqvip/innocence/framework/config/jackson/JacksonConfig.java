package com.cqvip.innocence.framework.config.jackson;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;


/**
 * @ClassName JacksonConfig
 * @Description 重新构建Jackson序列化方式
 * @Author Innocence
 * @Date 2020/8/26 11:27
 * @Version 1.0
 */
@Configuration
public class JacksonConfig {
    /**
     * Jackson全局转化long类型为String，解决jackson序列化时传入前端Long类型缺失精度问题
     * 新增时间转化
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        JavaTimeModule module = new JavaTimeModule();
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.modules(module);
            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializers(new ParsedStringTermsBucketSerializer(ParsedStringTerms.ParsedBucket.class));
        };
    }
}
