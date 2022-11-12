package com.mili.oss.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;

@RestController
public class ScmBFRedisHttpUtils {

    @Autowired
	private   RestTemplate restTemplate;
    
	public   Object redisGet(String url,String key) {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("key", key);
		map.put("hash", false);
		String s =   restTemplate.postForObject(url, map,String.class);
        if(StringUtils.isBlank(s)){
            return null;
        }
        ResponseResult result = JSONObject.parseObject(s, ResponseResult.class);
        if(!result.isSuccess()) {
        	return null;
        }
        return result.getData();
	}
	
	public   Object redisGet(String url,String key,String hashKey) {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("key", key);
		map.put("hash", true);
		map.put("hkey", hashKey);
		String s =   restTemplate.postForObject(url, map,String.class);
        if(StringUtils.isBlank(s)){
            return "";
        }
        ResponseResult result = JSONObject.parseObject(s, ResponseResult.class);
        if(!result.isSuccess()) {
        	return "";
        }
        return result.getData();
	}
	
	public   Object redisinner(String url,String key,Integer incrVal) {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("key", key);
		map.put("incrVal", incrVal);
		String s = restTemplate.postForObject(url, map,String.class);
        if(StringUtils.isBlank(s)){
            return null;
        }
        ResponseResult result = JSONObject.parseObject(s, ResponseResult.class);
        if(!result.isSuccess()) {
        	return null;
        }
        return result.getData();
	}
}
