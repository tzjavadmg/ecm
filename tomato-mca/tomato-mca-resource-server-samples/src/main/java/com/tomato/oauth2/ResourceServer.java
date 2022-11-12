package com.tomato.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EnableOAuth2Client
public class ResourceServer {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServer.class, args);
    }
}
