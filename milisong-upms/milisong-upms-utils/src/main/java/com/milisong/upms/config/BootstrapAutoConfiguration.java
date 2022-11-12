package com.milisong.upms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.milisong.upms.service.HttpService;
import com.milisong.upms.utils.RestClient;
import com.milisong.upms.utils.UserInfoUtil;


/**
 * 工具类引导装配类
 * @author yangzhilong
 *
 */
@Configuration
public class BootstrapAutoConfiguration {
	@Value("${rest.config.connectTimeout:10000}")
	private int connectTimeout;
	@Value("${rest.config.readTimeout:30000}")
	private int readTimeout;
	@Autowired
    private HttpService httpService;
	
	/**
	 * 使用Bootstrap来装配RestClient中的RestTemplate属性，
	 * 避免直接装配RestTemplate来污染了正常的spring Cloud的调用
	 * @return
	 */
	@Bean
    public MyBootstrap myBootstrap(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(connectTimeout);
        httpRequestFactory.setReadTimeout(readTimeout);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        RestClient.setRestTemplate(restTemplate);
        
        UserInfoUtil.setHttpService(httpService);
        
        return new MyBootstrap();
    }
	
	/**
	 * 空的引导类
	 * @author yangzhilong
	 */
	static class MyBootstrap {
		
	}
}
