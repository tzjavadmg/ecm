package com.milisong.ecm.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/4 14:14
 */
@Configuration
public class EcmConfig {

    @Bean
    public RestTemplate restTemplate(){
        //HttpClientBuilder httpClientBuilder = HttpClients.custom();
        //httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        //HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000);
        httpRequestFactory.setConnectTimeout(30000);
        httpRequestFactory.setReadTimeout(30000);
        return new RestTemplate(httpRequestFactory);
    }
}
