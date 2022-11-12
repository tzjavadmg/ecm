package com.milisong.pos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
*@author    created by benny
*@date  2018年10月24日---下午7:32:28
*
*/

@SpringBootApplication(scanBasePackages = { "com.farmland", "com.milisong" })
@MapperScan(basePackages = { "com.milisong.pos.production.mapper"})
@EnableTransactionManagement
@EnableEurekaClient
public class MilisongPosWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(MilisongPosWebApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}
}
