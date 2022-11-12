package com.tomato.mca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAuthorizationServer
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.tomato")
@ComponentScan(basePackages = {"com.tomato", "com.tomatoframework"})
public class AuthorizationServer {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServer.class, args);
    }

}
