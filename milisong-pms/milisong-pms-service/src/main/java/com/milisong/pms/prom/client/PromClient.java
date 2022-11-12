package com.milisong.pms.prom.client;

import com.milisong.pms.prom.util.ClientRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author sailor wang
 * @date 2018/11/8 3:25 PM
 * @description
 */
@Slf4j
@Service
public class PromClient {

    @Autowired
    ClientRestTemplate restTemplate;

    @Value("${ecm.send-advertise-sms:localhost}")
    private String sendAdvertiseSmsUrl;

    @Value("${ecm.send-breakfast-sms:localhost}")
    private String sendBreakfastSmsUrl;

    public void sendAdvertiseSms(Map<String,Object> sms){
        log.info("请求地址 -> {}",sendAdvertiseSmsUrl);
        restTemplate.doPost(sms,sendAdvertiseSmsUrl);
    }

    public void sendBreakfastSms(Map<String,Object> sms){
        log.info("请求地址 -> {}",sendBreakfastSmsUrl);
        restTemplate.doPost(sms,sendBreakfastSmsUrl);
    }


}