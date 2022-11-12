package com.milisong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/4 11:19
 */
@SpringBootApplication(scanBasePackages = {"com.farmland", "com.milisong"})
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@EnableTransactionManagement
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.milisong")
public class LunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(LunchApplication.class, args);
    }

}
