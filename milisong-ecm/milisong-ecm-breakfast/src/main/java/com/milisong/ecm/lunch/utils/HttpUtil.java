package com.milisong.ecm.lunch.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/4/12   11:33
 *    desc    :
 *    version : v1.0
 * </pre>
 */
@Slf4j
public class HttpUtil {

    private static RestTemplate restTemplate;

    public static void setRestTemplate(RestTemplate http) {
        restTemplate = http;
    }

    public static <T> T post(String url,Object obj,Class<T> tClass){
        URI uri = URI.create(url);
        log.info("--发出请求-{},参数->{}",url,obj);
        T post = restTemplate.postForObject(uri, obj, tClass);
        log.info("--响应结果-{}", JSON.toJSONString(post));
        return post;
    }

    public static <T> T get(String url,Class<T> tClass,Object...param){
        log.info("--发出请求-{},参数->{}",url,JSON.toJSONString(param));
        T result = restTemplate.getForObject(url, tClass, param);
        log.info("--响应结果-{}", JSON.toJSONString(result));
        return result;
    }

    public static <T> T get(String url,Class<T> tClass){
        log.info("--发出请求-{}",url);
        T result = restTemplate.getForObject(url, tClass);
        log.info("--响应结果-{}", JSON.toJSONString(result));
        return result;
    }

    public static <T> T post(String baseUrl,String url,Object obj,Class<T> tClass){
        return post(baseUrl.concat(url),obj,tClass);
    }

    public static <T> T get(String baseUrl,String url,Class<T> tClass,Object...param){
        return get(baseUrl.concat(url), tClass, param);
    }

    public static <T> T get(String baseUrl,String url,Class<T> tClass){
        return get(baseUrl.concat(url), tClass);
    }
}
