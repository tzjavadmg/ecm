package com.mili.oss.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaozhonghui
 * @date 2018-10-31
 */
@Slf4j
@Component
public class SmsTemplate {

    @Value("${sms.send-url}")
    private String sendMsgPath;
    private static final String businessId = "MILISONG";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    public ResponseResult<String> sendMsg(String mobile, String templateId, String signNo, String... params) {
        List<String> list = new ArrayList<>();
        list.add(mobile);
        return asyncSendMsg(list, templateId, signNo, params);
    }

    public ResponseResult<String> sendMsg(List<String> mobile, String templateId, String signNo, List<String> params) {
        String[] strings = new String[params.size()];
        params.toArray(strings);
//        return sendMsg(mobile, templateId, signNo, strings);
        return asyncSendMsg(mobile, templateId, signNo, strings);
    }

    public ResponseResult<String> sendMsg(List<String> mobiles, String templateCode, String signNo, String... params) {
        if (StringUtils.isBlank(templateCode) || CollectionUtils.isEmpty(mobiles)) {
            return ResponseResult.buildFailResponse("9999", "参数校验失败，缺少模版或者手机");
        }
        // 发送短信
        SmsDto smsDto = new SmsDto();
        List<String> mobileList = mobiles;
        smsDto.setTelephones(mobileList);
        smsDto.setTemplateCode(templateCode);
        if (params != null && params.length > 0) {
            smsDto.setParameters(Arrays.stream(params).collect(Collectors.toList()));
        }
        smsDto.setIp("127.0.0.1");
        smsDto.setSignNo(signNo);
        smsDto.setBusinessLine(businessId);
        try {
            log.info("【发送通知短信】：smsDto={}", JSONObject.toJSONString(smsDto));
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(smsDto), headers);
            ResponseResult<String> result = restTemplate.postForObject(sendMsgPath, entity, ResponseResult.class);
            log.info("【短信回执】：result={}", JSONObject.toJSONString(result));//result={"success":true}
            if (result.isSuccess()) {
                return result;
            } else {
                log.error(result.getCode(), result.getMessage());
                return result;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.buildFailResponse("9999", "发送短信失败");
        }
    }

    public ResponseResult<String> asyncSendMsg(List<String> mobiles, String templateCode, String signNo, String... params) {
        if (StringUtils.isBlank(templateCode) || CollectionUtils.isEmpty(mobiles)) {
            return ResponseResult.buildFailResponse("9999", "参数校验失败，缺少模版或者手机");
        }
        // 发送短信
        SmsDto smsDto = new SmsDto();
        List<String> mobileList = mobiles;
        smsDto.setTelephones(mobileList);
        smsDto.setTemplateCode(templateCode);
        if (params != null && params.length > 0) {
            smsDto.setParameters(Arrays.stream(params).collect(Collectors.toList()));
        }
        smsDto.setIp("127.0.0.1");
        smsDto.setSignNo(signNo);
        smsDto.setBusinessLine(businessId);
        try {
            log.info("【发送顺丰通知短信】：smsDto={}", JSONObject.toJSONString(smsDto));
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(smsDto), headers);
            ListenableFuture<ResponseEntity<ResponseResult>> forEntity = asyncRestTemplate.postForEntity(sendMsgPath, entity, ResponseResult.class);

            forEntity.addCallback(new ListenableFutureCallback<ResponseEntity<ResponseResult>>() {

                //调用失败
                @Override
                public void onFailure(Throwable ex) {
                    log.error("【发送短信失败】");
                }

                //调用成功
                @Override
                public void onSuccess(ResponseEntity<ResponseResult> result) {
                    log.info("【顺丰订单短信回执】：result={}", JSONObject.toJSONString(result.getBody()));
                    if (!result.getBody().isSuccess()) {
                        log.error(result.getBody().getCode(), result.getBody().getMessage());
                    }
                }
            });
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.buildFailResponse("9999", "发送短信失败");
        }
    }

    @Getter
    @Setter
    static final class SmsDto {
        private String templateCode;
        private List<String> telephones;
        private List<String> parameters;
        private String businessLine;
        private String ip;
        private String signNo;
    }
}
