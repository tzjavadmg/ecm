package com.milisong.pms.prom.util;

import com.farmland.core.api.ResponseResult;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 内网服务调用
 *
 * @author sailor wang
 * @date 2018/11/7 4:16 PM
 * @description
 */
@Service
public class ClientRestTemplate {

    @Autowired
    RestTemplate restTemplate;

    public ResponseResult<?> doPost(Map<String, Object> param, String url) {
        ResponseEntity<ResponseResult> responseEntity = restTemplate.postForEntity(url, param, ResponseResult.class);
        return responseEntity.getBody();
    }

    public ResponseResult<?> doGet(Map<String, Object> param, String url) {
        if (param != null){
            String params = Joiner.on("&").withKeyValueSeparator("=").join(param);
            url = url + "?"+ params;
        }
        ResponseEntity<ResponseResult> responseEntity = restTemplate.getForEntity(url, ResponseResult.class);
        return responseEntity.getBody();
    }
}