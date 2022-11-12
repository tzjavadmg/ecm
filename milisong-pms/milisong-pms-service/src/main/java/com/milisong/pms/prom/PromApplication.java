package com.milisong.pms.prom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author sailor wang
 * @date 2018/11/22 4:42 PM
 * @description
 */
@SpringBootApplication(scanBasePackages = {"com.farmland", "com.milisong"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients(basePackages = "com.milisong")
@MapperScan("com.milisong.pms.prom.mapper")
public class PromApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromApplication.class, args);
    }
}
