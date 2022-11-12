package com.milisong.ucs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@EnableFeignClients(basePackages = {"com.milisong"})
public class UcsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcsApplication.class, args);
    }
}
