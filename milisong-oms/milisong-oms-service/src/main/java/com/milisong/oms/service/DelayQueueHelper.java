package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.google.common.collect.Maps;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.constant.SystemModule;
import com.milisong.oms.dto.DelayMessageDto;
import com.milisong.oms.util.SysConfigUtils;
import com.milisong.oms.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/7 18:46
 */
@Slf4j
@Component
public class DelayQueueHelper {
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private RestTemplate restTemplate;

    public void push(Long orderId, Date orderDate) {
        try {
            DelayMessageDto messageDto = new DelayMessageDto();
            Map<String, Object> map = Maps.newHashMap();
            map.put("orderId", orderId);
            map.put("orderDate", DateFormatUtils.format(orderDate, "yyyy-MM-dd HH:mm:ss"));
            messageDto.setSystem(SystemModule.SYS_OMS.getValue());
            messageDto.setModule(SystemModule.MOD_ORDER.getValue());
            messageDto.setBizId(orderId);
            messageDto.setBody(JSON.toJSONString(map));
            messageDto.setCallbackUrl(systemProperties.getDelayQueue().getOrderCallbackUrl());
            messageDto.setTtl(SysConfigUtils.getUnPayExpiredTime() * 60L);
            String postUrl = systemProperties.getDelayQueue().getBaseUrl() + "/v1/mq/send-delay-message";
            HttpClientUtils.postForDelayQueue(restTemplate, messageDto, postUrl);
        } catch (Exception e) {
            log.error("----真实订单发送延迟队列消息---------延迟队列服务异常----", e);
        }
    }

    public void remove(Long orderId) {
        try {
            DelayMessageDto messageDto = new DelayMessageDto();
            messageDto.setSystem(SystemModule.SYS_OMS.getValue());
            messageDto.setModule(SystemModule.MOD_ORDER.getValue());
            messageDto.setBizId(orderId);
            String postUrl = systemProperties.getDelayQueue().getBaseUrl() + "/v1/mq/cancel-message";
            HttpClientUtils.postForDelayQueue(restTemplate, messageDto, postUrl);
        } catch (Exception e) {
            log.error("----真实订单取消延迟队列消息---------延迟队列服务异常----", e);
        }
    }

    public void pushGroupBuy(Long orderId, Date orderDate) {
        try {
            DelayMessageDto messageDto = new DelayMessageDto();
            Map<String, Object> map = Maps.newHashMap();
            map.put("orderId", orderId);
            map.put("orderDate", DateFormatUtils.format(orderDate, "yyyy-MM-dd HH:mm:ss"));
            messageDto.setSystem(SystemModule.SYS_OMS.getValue());
            messageDto.setModule(SystemModule.MOD_GROUP_BUY_ORDER.getValue());
            messageDto.setBizId(orderId);
            messageDto.setBody(JSON.toJSONString(map));
            messageDto.setCallbackUrl(systemProperties.getDelayQueue().getGroupBuyOrderCallbackUrl());
            messageDto.setTtl(SysConfigUtils.getGroupBuyPayExpiredTime());
            String postUrl = systemProperties.getDelayQueue().getBaseUrl() + "/v1/mq/send-delay-message";
            HttpClientUtils.postForDelayQueue(restTemplate, messageDto, postUrl);
        } catch (Exception e) {
            log.error("----拼团订单发送延迟队列消息---------延迟队列服务异常----", e);
        }
    }

    public void removeGroupBuy(Long orderId) {
        try {
            DelayMessageDto messageDto = new DelayMessageDto();
            messageDto.setSystem(SystemModule.SYS_OMS.getValue());
            messageDto.setModule(SystemModule.MOD_GROUP_BUY_ORDER.getValue());
            messageDto.setBizId(orderId);
            String postUrl = systemProperties.getDelayQueue().getBaseUrl() + "/v1/mq/cancel-message";
            HttpClientUtils.postForDelayQueue(restTemplate, messageDto, postUrl);
        } catch (Exception e) {
            log.error("----拼团订单取消延迟队列消息---------延迟队列服务异常----", e);
        }
    }

    public void pushVirtual(Long orderId, Date orderDate) {
        try {
            DelayMessageDto messageDto = new DelayMessageDto();
            Map<String, Object> map = Maps.newHashMap();
            map.put("orderId", orderId);
            map.put("orderDate", DateFormatUtils.format(orderDate, "yyyy-MM-dd HH:mm:ss"));
            messageDto.setSystem(SystemModule.SYS_OMS.getValue());
            messageDto.setModule(SystemModule.MOD_VIRTUAL_ORDER.getValue());
            messageDto.setBizId(orderId);
            messageDto.setBody(JSON.toJSONString(map));
            messageDto.setCallbackUrl(systemProperties.getDelayQueue().getVirtualOrderCallbackUrl());
            messageDto.setTtl(SysConfigUtils.getUnPayExpiredTime() * 60L);
            String postUrl = systemProperties.getDelayQueue().getBaseUrl() + "/v1/mq/send-delay-message";
            HttpClientUtils.postForDelayQueue(restTemplate, messageDto, postUrl);
        } catch (Exception e) {
            log.error("----虚拟订单发送延迟队列消息---------延迟队列服务异常----", e);
        }
    }

    public void removeVirtual(Long orderId) {
        try {
            DelayMessageDto messageDto = new DelayMessageDto();
            messageDto.setSystem(SystemModule.SYS_OMS.getValue());
            messageDto.setModule(SystemModule.MOD_VIRTUAL_ORDER.getValue());
            messageDto.setBizId(orderId);
            String postUrl = systemProperties.getDelayQueue().getBaseUrl() + "/v1/mq/cancel-message";
            HttpClientUtils.postForDelayQueue(restTemplate, messageDto, postUrl);
        } catch (Exception e) {
            log.error("----虚拟订单取消延迟队列消息---------延迟队列服务异常----", e);
        }
    }

    /**
     * 获取订单待支付倒计时
     *
     * @param orderId
     * @return
     */
    public Long getOrderExpireTime(Long orderId) {
        long expireTime = 0;
        DelayMessageDto messageDto = new DelayMessageDto();
        messageDto.setSystem(SystemModule.SYS_OMS.getValue());
        messageDto.setModule(SystemModule.MOD_ORDER.getValue());
        messageDto.setBizId(orderId);
        String postUrl = systemProperties.getDelayQueue().getBaseUrl() + "/v1/mq/get-expire-time";
        ResponseResult<Integer> result = HttpClientUtils.postForDelayQueue(restTemplate, messageDto, postUrl);
        if (result.isSuccess()) {
            expireTime = result.getData();
        }
        return expireTime;
    }
}
