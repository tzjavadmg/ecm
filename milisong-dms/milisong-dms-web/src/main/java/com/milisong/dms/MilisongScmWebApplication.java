package com.milisong.dms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.AsyncRestTemplateCustomizer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = { "com.farmland", "com.milisong"})
@MapperScan(basePackages = { "com.milisong.dms.mapper" })
@EnableTransactionManagement
@EnableFeignClients(basePackages= {"com.milisong"})
public class MilisongScmWebApplication {
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Bean
	public RestTemplate restTemplate(){
		return restTemplateBuilder.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(MilisongScmWebApplication.class, args);
	}
}
