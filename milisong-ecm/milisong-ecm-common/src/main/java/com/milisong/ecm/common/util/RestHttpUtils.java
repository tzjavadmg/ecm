package com.milisong.ecm.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/5   11:40
 *    desc    : http工具类
 *    version : v1.0
 * </pre>
 */
@Slf4j
public class RestHttpUtils {

    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    private static final String ACCEPT = "Accept";

    public static ResponseResult<Object> post(RestTemplate restTemplate, String url, Object dto){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MEDIA_TYPE);
        headers.setContentType(type);
        headers.add(ACCEPT, MediaType.APPLICATION_JSON.toString());
        HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(dto),headers);
        JSONObject result = restTemplate.postForObject(url, entity, JSONObject.class);
        ResponseResult<Object> responseResult = new ResponseResult<>();
        log.info("----接口返回信息---{}",JSON.toJSONString(responseResult));
        responseResult.setSuccess(result.getBoolean("success"));
        responseResult.setCode(result.getString("code"));
        responseResult.setMessage(result.getString("message"));
        responseResult.setDetailMessage(result.getString("detailMessage"));
        Object data = result.get("data");
        if(data != null){
            responseResult.setData(data);
        }
        if (responseResult.isSuccess()) {
            return responseResult;
        } else {
            log.error(responseResult.getCode(), responseResult.getMessage());
            return responseResult;
        }
    }

    public static String postForString(RestTemplate restTemplate, String url, Object dto){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MEDIA_TYPE);
        headers.setContentType(type);
        headers.add(ACCEPT, MediaType.APPLICATION_JSON.toString());
        HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(dto),headers);
        return restTemplate.postForObject(url, entity,String.class);
    }

    public static <T> ResponseResult<T> get(RestTemplate restTemplate, String url, Map<String,?> uriParam){
        ResponseResult<T> result = restTemplate.getForObject(url,ResponseResult.class,uriParam);
        log.info("----接口返回信息---{}",JSON.toJSONString(result));
        if (result.isSuccess()) {
            return result;
        } else {
            log.error(result.getCode(), result.getMessage());
            return result;
        }
    }

    public static String getForString(RestTemplate restTemplate, String url, Map<String,?> uriParam){
        String result = restTemplate.getForObject(url,String.class,uriParam);
        log.info("----接口返回信息---{}",JSON.toJSONString(result));
        return result;
    }
    
    public static <T> T postForObjectForLunch(RestTemplate restTemplate, String url, Object dto,Class<T> tClass){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MEDIA_TYPE);
        headers.setContentType(type);
        headers.add(ACCEPT, MediaType.APPLICATION_JSON.toString());
        HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(dto),headers);
        T result = restTemplate.postForObject(url, entity,tClass);
        return result;
    }

}
