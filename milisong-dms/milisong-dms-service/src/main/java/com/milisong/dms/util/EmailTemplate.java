package com.milisong.dms.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author zhaozhonghui
 * @date 2018-08-20
 */
@Slf4j
@Component
public class EmailTemplate {
    @Value("${mail.send-url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult<String> sendEmail(Map<String,Object> mailMap){

        try {
            log.info("【发送邮件】：参数={}", JSONObject.toJSONString(mailMap));
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(mailMap),headers);
            ResponseResult<String> result = restTemplate.postForObject(url, entity,ResponseResult.class);
            log.info("【邮件回执】：result={}", JSONObject.toJSONString(result));//result={"success":true}
            if (result.isSuccess()) {
                return result;
            } else {
                log.error(result.getCode(), result.getMessage());
                return result;
            }
        }catch (Exception e){
            log.error("发送邮件失败!", e);
            return ResponseResult.buildFailResponse("发送邮件失败！");
        }
    }
}
