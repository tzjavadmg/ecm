package com.milisong.delay.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.farmland.core.cache.RedisCache;
import com.milisong.delay.dto.MessageDto;
import com.milisong.delay.enums.StatusEnum;
import com.milisong.delay.service.CallBackService;
import com.milisong.delay.service.DataService;
import com.milisong.delay.util.DateUtil;
import com.milisong.delay.util.RedisKeyUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 延迟队列消息消费
 */
@Component
@RabbitListener(queues = RabbitConfig.PROCESS_QUEUE)
@Slf4j
public class DelayQueueConsumer {
	@Autowired
	private CallBackService callBackService;
	
	@Autowired
	private DataService dataService;
	
	
	@RabbitHandler
	public void accept(MessageDto message) {
		log.info("消费消息时间={},内容={}",DateUtil.getCurrentDate(),JSON.toJSONString(message));
		String consumeMessageKey = RedisKeyUtil.getConsumeMessageIdKey(message.getMsgId());
		String resultMessage = RedisCache.get(consumeMessageKey);
		if (StringUtils.isEmpty(resultMessage)) {
			log.info("消息已暂停或已取消,无需回调业务系统,msgId={}",message.getMsgId());
			return ;
		}
		byte status = StatusEnum.SUCCESS.getStatus();
        String messageExpireKey = RedisKeyUtil.getMessageExpireKey(message.getMsgId());
		String key = RedisKeyUtil.getIncrCallBackKey(messageExpireKey);
		long incr = RedisCache.incrBy(key, 1);
		log.info("mq回调业务系统={}",incr);
		try {
			if (incr == 1) {
				//回调业务系统
				boolean callResult = callBackService.callBack(message);
				log.info("mq回调业务系统返回结果{}",callResult);
				if (!callResult) {
					status = StatusEnum.FAIL.getStatus();
				}
				//重试次数不累计发送次数
				dataService.insertConsume(message, status); 
			}

		}catch(Exception e) {
			status = StatusEnum.FAIL.getStatus();
			e.printStackTrace();
            log.error("MQ消息回调业务系统异常", message.getBody());
		}finally {
			if (incr>1) {
				RedisCache.delete(key);
			}
			RedisCache.delete(consumeMessageKey);
		}
		
	}
}
