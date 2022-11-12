package com.milisong.pms.prom.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.milisong.pms.prom.dto.DelayQueueMessage;
import com.milisong.pms.prom.enums.SystemModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 拼团延迟队列
 *
 * @author sailor wang
 * @date 2019/5/21 7:55 PM
 * @description
 */
@Slf4j
@Component
public class GroupBuyDelayQueueUtil {
    @Resource
    private RestTemplate restTemplate;

    @Value("${delay.cancel-groupbuy-callback-url}")
    private String cancelGroupbuyCallbackUrl;

    @Value("${delay.notify-groupbuy-callback-url}")
    private String notifyGroupbuyCallbackUrl;


    @Value("${delay.base-url}")
    private String baseUrl;

    public void refundGroupBuy(Long userGroupBuyId,long seconds) {
        DelayQueueMessage message = new DelayQueueMessage();
        Map<String, Object> body = Maps.newHashMap();
        body.put("userGroupBuyId", userGroupBuyId);
        message.setSystem(SystemModule.SYS_PMS.getValue());
        message.setModule(SystemModule.MOD_GROUPBUY.getValue());
        message.setBizId(userGroupBuyId);
        message.setBody(JSON.toJSONString(body));
        message.setCallbackUrl(cancelGroupbuyCallbackUrl);
        message.setTtl(seconds);
        String postUrl = baseUrl + "/v1/mq/send-delay-message";
        log.info("退款注册延迟队列，message -> {}",message);
        HttpClientUtils.postForDelayQueue(restTemplate, message, postUrl);
    }

    /**
     * 拼团中的活动，30分钟倒计时提醒。PS:该功能暂时去掉，拼团有效时长如果改为30分钟会和30分钟的成团提醒有冲突
     *
     * @param userGroupBuyId
     * @param seconds
     */
//    @Deprecated
//    public void leftTimeNotifyGroupBuy(Long userGroupBuyId, long seconds) {
//        DelayQueueMessage message = new DelayQueueMessage();
//        Map<String, Object> body = Maps.newHashMap();
//        body.put("userGroupBuyId", userGroupBuyId);
//        message.setSystem(SystemModule.SYS_PMS.getValue());
//        message.setModule(SystemModule.MOD_GROUPBUY.getValue());
//        message.setBizId(userGroupBuyId);
//        message.setBody(JSON.toJSONString(body));
//        message.setCallbackUrl(notifyGroupbuyCallbackUrl);
//        message.setTtl(seconds);
//        String postUrl = baseUrl + "/v1/mq/send-delay-message";
//        log.info("倒计时提醒注册延迟队列，message -> {}",message);
//        HttpClientUtils.postForDelayQueue(restTemplate, message, postUrl);
//    }
}