package com.milisong.delay.service;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.delay.dto.MessageDto;
import com.milisong.delay.enums.StatusEnum;
import com.milisong.delay.mq.RabbitConfig;
import com.milisong.delay.util.DateUtil;
import com.milisong.delay.util.RedisKeyUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DelaySendService {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private DataService dataService;

	
    /**
     * 发送延时消息，每个消息都自己有自己的过期时间
     * @param msg 消息
     */
    public ResponseResult<String> sendDelayMessage(MessageDto msg) {
    	log.info("发送延时消息时间={},业务id={}",DateUtil.getCurrentDate(),msg.getBizId());
    	ResponseResult<String> responseResult = ResponseResult.buildSuccessResponse();
    	byte status = StatusEnum.SUCCESS.getStatus();
    	//消息产生一个唯一表示(同一个业务多次开始暂停)
    	String incrMessageKey = RedisKeyUtil.getIncrMesssageKey(msg.getSystem(),msg.getModule(),msg.getBizId());
    	long incrMessage = RedisCache.incrBy(incrMessageKey, 1);
    	RedisCache.expire(incrMessageKey, 7, TimeUnit.DAYS);
    	msg.setMsgId(RedisKeyUtil.getMessageId(msg.getSystem(),msg.getModule(),msg.getBizId(), incrMessage));
    	try {
            rabbitTemplate.convertAndSend(RabbitConfig.DELAY_EXCHANGE, RabbitConfig.DELAY_ROUTING_KEY, msg,
                    message -> {
                    	Long ttl = msg.getTtl()*1000;
                    	String converTtl = String.valueOf(ttl);
                    	msg.setTtl(ttl);
                    	log.info("消息过期时间：{}",converTtl);
                        message.getMessageProperties().setExpiration(converTtl);
                        return message;
                    });
            String consumeMessageIdKey = RedisKeyUtil.getConsumeMessageIdKey(msg.getMsgId());
            Map<String,Object> redisMap = new HashMap<>();
            redisMap.put("currentTime", System.currentTimeMillis());
            redisMap.put("ttl", msg.getTtl());
            //用于暂停后，当前消息不回调业务系统(暂停时删除该消息)
            RedisCache.setEx(consumeMessageIdKey, JSON.toJSONString(redisMap), 7, TimeUnit.DAYS);
            
            //设置消息过期时间，用于获取消息剩余时间的接口
            String messageExpireKey = RedisKeyUtil.getMessageExpireKey(msg.getMsgId());
            RedisCache.setEx(messageExpireKey, msg.getBody(), msg.getTtl(), TimeUnit.MILLISECONDS);
            
            //根据key存放回调地址，用于过期时通知业务系统
			String keyPrefix = RedisKeyUtil.getBasisDataKey(messageExpireKey);
			RedisCache.set(keyPrefix, JSONObject.toJSONString(msg));
        } catch (AmqpException e) {
        	status = StatusEnum.FAIL.getStatus();
        	responseResult.setSuccess(false);
            log.error("消息发送失败，请检查消息中间件是否正常", JSONObject.toJSONString(msg));
        }
    	dataService.insertProduction(msg, status,1);
        return responseResult;
    }

    /**
     * 发送消息，至指定过期时间的队列中。
     * 适用于一些定时任务
     * @param msg
     */
    public ResponseResult<String> sendDelayQueueMessage(MessageDto msg) {
    	log.info("发送延时队列消息时间={},消息id={}",DateUtil.getCurrentDate(),msg.getMsgId());
    	ResponseResult<String> responseResult = ResponseResult.buildSuccessResponse();
    	byte status = StatusEnum.SUCCESS.getStatus();
    	try {
            rabbitTemplate.convertAndSend(RabbitConfig.DELAY_QUEUE_EXCHANGE, RabbitConfig.DELAY_ROUTING_KEY, msg);
        } catch (AmqpException e) {
        	status = StatusEnum.FAIL.getStatus();
        	responseResult.setSuccess(false);
            log.error("消息发送失败，请检查消息中间件是否正常", JSONObject.toJSONString(msg));
        }
    	dataService.insertProduction(msg, status, 1);
        return responseResult;
    }
    


}
