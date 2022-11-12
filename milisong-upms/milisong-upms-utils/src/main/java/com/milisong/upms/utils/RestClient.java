package com.milisong.upms.utils;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

/**
 * HTTP请求帮助类
 * @author yangzhilong
 */
public class RestClient {

    private static RestTemplate restTemplate;
    
    /**
     * 获取RestTemplate
     * @return
     */
    public static RestTemplate getRestTemplate() {
    	return restTemplate;
    }

    /**
     * 注入实现类
     * @param client
     */
    public static void setRestTemplate(RestTemplate client) {
    	restTemplate = client;
    }
    
    /**
     * 发GET请求
     * @param url
     * @return
     */
	public static String get(String url) {
        return restTemplate.getForObject(url, String.class);
    }
	
	/**
	 * 发送POST请求
	 * @param url
	 * @param obj
	 * @return
	 */
	public static String post(String url, Object obj) {
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String result = null;
        if(obj == null){
            result = "{}";
        }else{
            result = JSON.toJSONString(obj);
        }
        HttpEntity<String> formEntity = new HttpEntity<String>(result,headers);
        return restTemplate.postForObject(url , formEntity, String.class);
	}
}
