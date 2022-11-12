package com.mili.oss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;


/**
 * @author benny
 *
 */
@SpringBootApplication(scanBasePackages = {"com.farmland", "com.mili", "com.milisong"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@MapperScan(basePackages ="com.mili.oss.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mili", "com.milisong"})
public class OssApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000);
        httpRequestFactory.setConnectTimeout(30000);
        httpRequestFactory.setReadTimeout(30000);
        return new RestTemplate(httpRequestFactory);
    }
}
