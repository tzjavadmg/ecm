package com.milisong.breakfast.pos.timeout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.milisong.breakfast.pos.api.PosProductionService;
import com.milisong.breakfast.pos.constant.OrderSetStatusEnum;
import com.milisong.breakfast.pos.constant.RedisKeyConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TopicMessageListener implements MessageListener {
	@Autowired
	private PosProductionService posProductionService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        // redis的value
        String key = new String(body);
        log.info("接收过期通知,key={}",  key);
        
        // 是集单的超时key
        if(key.startsWith(RedisKeyConstant.ORDER_SET_TIMEOUT_TTL)) {
        	log.info("redis key：{} is timeout", key);
        	String[] arr = key.split(RedisKeyConstant.SEPARATE);
        	String shopId = arr[1];
        	String orderSetNo = arr[2];
        	
        	posProductionService.updateOrderSetStatusByNo(Long.valueOf(shopId), orderSetNo, OrderSetStatusEnum.FINISH_ORDER_3.getValue(),"system_系统");
        }
    }
}
