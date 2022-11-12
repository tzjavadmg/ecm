package com.milisong.scm.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(value = { "com.milisong", "com.farmland"})
@MapperScan(basePackages = { "com.milisong.scm.base.mapper"})
@EnableFeignClients({"com.milisong"})
@EnableTransactionManagement
@EnableCaching
@EnableDiscoveryClient
public class MilisongScmBaseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MilisongScmBaseServiceApplication.class,args);
    }
}
