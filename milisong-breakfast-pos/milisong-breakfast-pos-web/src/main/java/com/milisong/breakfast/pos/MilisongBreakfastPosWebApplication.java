package com.milisong.breakfast.pos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = { "com.farmland", "com.milisong" })
@MapperScan(basePackages = { "com.milisong.breakfast.pos.mapper" })
@EnableTransactionManagement
@EnableEurekaClient
public class MilisongBreakfastPosWebApplication {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	@Bean
	public RestTemplate restTemplate(){
		return restTemplateBuilder.build();
	}
	public static void main(String[] args) {
		SpringApplication.run(MilisongBreakfastPosWebApplication.class, args);
	}
}
