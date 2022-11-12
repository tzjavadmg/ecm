package com.milisong.upms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhaozhonghui
 * @date 2018-11-07
 */
@SpringBootApplication
@ComponentScan(value = { "com.milisong", "com.farmland" })
@EnableTransactionManagement
public class MilisongUpmsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MilisongUpmsServiceApplication.class,args);
    }
}
