package com.milisong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/30 18:40
 */
@SpringBootApplication(scanBasePackages = {"com.farmland", "com.milisong"})
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@EnableTransactionManagement
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.milisong")
public class BreakfastApplication {

    public static void main(String[] args) {
        SpringApplication.run(BreakfastApplication.class, args);
    }
}
