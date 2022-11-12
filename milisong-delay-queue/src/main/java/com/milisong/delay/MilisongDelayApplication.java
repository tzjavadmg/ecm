package com.milisong.delay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/4 11:19
 */
@SpringBootApplication(scanBasePackages = {"com.farmland", "com.milisong.delay"})
public class MilisongDelayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilisongDelayApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
