package com.cqvip.innocence.framework.config.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ElasticSearchClientConfig
 * @Description yml形式配置es的超时无效，因此用配置类的形式进行相关配置
 * @Author Innocence
 * @Date 2021/4/9 14:07
 * @Version 1.0
 */
@Configuration
public class ElasticSearchClientConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private String port;

    @Value("${elasticsearch.scheme}")
    private String scheme;

    @Value("${elasticsearch.connect-timeout}")
    private Integer connectTimeout;

    @Value("${elasticsearch.socket-timeout}")
    private Integer socketTimeout;

    @Bean
    @Qualifier("highLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        List<HttpHost> hosts = new ArrayList<>();
        if (port.indexOf(",") != -1){
            String[] split = port.split(",");
            for (String s : split) {
                Integer p = Integer.valueOf(s);
                hosts.add(new HttpHost(host, p, scheme));
            }
        }else {
            hosts.add(new HttpHost(host, Integer.valueOf(port), scheme));
        }
        // 该方法接收一个RequestConfig.Builder对象，对该对象进行修改后然后返回。
        RestHighLevelClient highLevelClient = new RestHighLevelClient(
                RestClient.builder(hosts.toArray(new HttpHost[hosts.size()]))
                        .setRequestConfigCallback(requestConfigBuilder -> {
                            return requestConfigBuilder.setConnectTimeout(connectTimeout) // 连接超时（默认为1秒）
                                    .setSocketTimeout(socketTimeout);// 套接字超时（默认为30秒）//更改客户端的超时限制默认30秒现在改为100*1000分钟
                        }));// 调整最大重试超时时间（默认为30秒）.setMaxRetryTimeoutMillis(60000);

        return highLevelClient;
    }
}
