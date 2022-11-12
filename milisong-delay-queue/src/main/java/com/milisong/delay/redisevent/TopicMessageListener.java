package com.milisong.delay.redisevent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.delay.dto.MessageDto;
import com.milisong.delay.enums.StatusEnum;
import com.milisong.delay.service.CallBackService;
import com.milisong.delay.service.DataService;
import com.milisong.delay.util.RedisKeyUtil;


@Slf4j
@Component
public class TopicMessageListener implements MessageListener {
	
	@Autowired
	private CallBackService callBackService;
	
	@Autowired
	private DataService dataService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String topic = new String(channel);
        String value = new String(body);
        log.info("接收过期通知,topic={},key={}", topic, value);
        String[] keys = value.split(":");
        String prefixKey = keys[0];
        //过滤服务本身过期key
        if (RedisKeyUtil.INCR_MESSAGE.equals(prefixKey)||
        	RedisKeyUtil.CONSUME_MEESAGE_ID.equals(prefixKey)) {
        	return ;
        }
        String keyPrefix = RedisKeyUtil.getBasisDataKey(value);
        String result = RedisCache.get(keyPrefix);
        MessageDto messageDto = JSONObject.parseObject(result, MessageDto.class);
		byte status = StatusEnum.SUCCESS.getStatus();
		String key = RedisKeyUtil.getIncrCallBackKey(value);
		long incr = RedisCache.incrBy(key, 1);
		log.info("redis回调业务系统={}",incr);
		try {
			if (incr == 1) {
				String msgId = value.substring(value.indexOf(":")+1, value.length());
				String consumeMessageKey = RedisKeyUtil.getConsumeMessageIdKey(msgId);
				String resultMessage = RedisCache.get(consumeMessageKey);
				if (StringUtils.isEmpty(resultMessage)) {
					log.info("消息已暂停或已取消,无需回调业务系统,msgId={}",msgId);
					return ;
				}
				//回调业务系统
				boolean callResult = callBackService.callBack(messageDto);
				log.info("redis回调业务系统结果{}",callResult);
				if (!callResult) {
					status = StatusEnum.FAIL.getStatus();
				}
				dataService.insertConsume(messageDto, status); 
			}
		}catch(Exception e) {
			status = StatusEnum.FAIL.getStatus();
			e.printStackTrace();
            log.error("redis过期key回调业务系统异常", message.getBody());
		}finally {
			if (incr>1) {
				RedisCache.delete(key);
			}
			RedisCache.delete(keyPrefix);
		}
     }
}
