package com.milisong.breakfast.scm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = { "com.farmland", "com.milisong" })
@MapperScan(basePackages = { "com.milisong.breakfast.scm.common.mapper", "com.milisong.breakfast.scm.goods.mapper",
		"com.milisong.breakfast.scm.order.mapper", "com.milisong.breakfast.scm.orderset.mapper",
		"com.milisong.breakfast.scm.shop.mapper", "com.milisong.breakfast.scm.configuration.mapper" })
@EnableTransactionManagement
@EnableEurekaClient
@EnableFeignClients(basePackages= {"com.milisong"})
public class MilisongBreakfastScmWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilisongBreakfastScmWebApplication.class, args);
	}
	
    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000);
        httpRequestFactory.setConnectTimeout(30000);
        httpRequestFactory.setReadTimeout(30000);
        return new RestTemplate(httpRequestFactory);
    }
}
