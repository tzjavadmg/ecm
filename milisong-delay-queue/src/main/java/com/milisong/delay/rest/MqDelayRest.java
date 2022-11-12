package com.milisong.delay.rest;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.JsonMapper;
import com.milisong.delay.domain.DelayConsumeEvent;
import com.milisong.delay.dto.MessageDto;
import com.milisong.delay.dto.TestDto;
import com.milisong.delay.enums.SysEnum;
import com.milisong.delay.service.CallBackService;
import com.milisong.delay.service.DataService;
import com.milisong.delay.service.DelaySendService;
import com.milisong.delay.util.RedisKeyUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/v1/mq")
public class MqDelayRest {
	
	@Autowired
	private DelaySendService delaySendService;
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private CallBackService callBackService;
	/**
	 * 发送延时消息，每个消息都有自己的过期时间
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "/send-delay-message" , method = RequestMethod.POST)
	public ResponseResult<String> sendDelayMessage(@RequestBody MessageDto message) {
		log.info("发送延时消息{}",JSON.toJSONString(message));
		return delaySendService.sendDelayMessage(message);
	}
	
	/**
	 * 发送延时消息队列，整个队列有自己的过期时间
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "/send-delay-queue-message" , method = RequestMethod.POST)
	public ResponseResult<String> sendDelayQueueMessage(@RequestBody MessageDto message) {
		log.info("发送延时消息队列{}",JSON.toJSONString(message));
		return delaySendService.sendDelayQueueMessage(message);
	}
	
	/**
	 * 暂停消息
	 * @param msgId
	 * @return
	 */
	@RequestMapping(value = "/pause-delay-message" , method = RequestMethod.POST)
	public ResponseResult<Long> pauseDelayMessage(@RequestBody MessageDto message) {
		log.info("暂停消息,bizId={},module={}",message.getBizId(),message.getModule());
		if (null == message.getBizId() || StringUtils.isBlank(message.getModule())) {
			return ResponseResult.buildFailResponse(SysEnum.PARAM_VALIDATE_NULL.getCode(), SysEnum.PARAM_VALIDATE_NULL.getName());
		}
		if (StringUtils.isBlank(message.getSystem()) && "production".equals(message.getModule())) {
			message.setSystem("pos");
		}
		try{
			//获取最新一条消息值
			String incrMessageKey = RedisKeyUtil.getIncrMesssageKey(message.getSystem(),message.getModule(),message.getBizId());
			String incrMessageValue = RedisCache.get(incrMessageKey);
			if (StringUtils.isEmpty(incrMessageValue)) {
				return ResponseResult.buildFailResponse(SysEnum.MESSAGE_IS_EXPIRE.getCode(),SysEnum.MESSAGE_IS_EXPIRE.getName());
			}
			//获取消息剩余时间
			String msgId = RedisKeyUtil.getMessageId(message.getSystem(),message.getModule(),message.getBizId(), Long.valueOf(incrMessageValue));
			String messageExpireKey = RedisKeyUtil.getMessageExpireKey(msgId);
			Long remainderTime = RedisCache.getExpire(messageExpireKey, TimeUnit.SECONDS);
			//删除消息过期key
			RedisCache.delete(messageExpireKey);
			String consumeMessageKey = RedisKeyUtil.getConsumeMessageIdKey(msgId);
			//暂停后删除消息表示，此次消息将不被回调业务系统
			RedisCache.delete(consumeMessageKey);
			return ResponseResult.buildSuccessResponse(remainderTime);
		}catch(Exception e) {
			log.error("暂停消息异常",e);
			return ResponseResult.buildFailResponse();
		}
		
	}
	
	/**
	 * 获取消息过期所剩余时间
	 * @param msgId
	 * @return
	 */
	@RequestMapping(value = "/get-expire-time" , method = RequestMethod.POST)
	public ResponseResult<Long> getExipreTime(@RequestBody MessageDto message) {
		log.info("获取消息过期时间bizId={},module={}",message.getBizId(),message.getModule());
		if (null == message.getBizId() || StringUtils.isBlank(message.getModule())) {
			return ResponseResult.buildFailResponse(SysEnum.PARAM_VALIDATE_NULL.getCode(), SysEnum.PARAM_VALIDATE_NULL.getName());
		}
		if (StringUtils.isBlank(message.getSystem()) && "production".equals(message.getModule())) {
			message.setSystem("pos");
		}
		try{
			String incrMessageKey = RedisKeyUtil.getIncrMesssageKey(message.getSystem(),message.getModule(),message.getBizId());
			String incrMessageValue = RedisCache.get(incrMessageKey);
			if (StringUtils.isEmpty(incrMessageValue)) {
				return ResponseResult.buildSuccessResponse(0L);
			}
			String msgId = RedisKeyUtil.getMessageId(message.getSystem(),message.getModule(),message.getBizId(), Long.valueOf(incrMessageValue));
			String messageExpireKey = RedisKeyUtil.getMessageExpireKey(msgId);
			Long remainderTime = RedisCache.getExpire(messageExpireKey, TimeUnit.SECONDS);
			//key已过期
			if (remainderTime == -2) {
				remainderTime = 0l;
			}
			return ResponseResult.buildSuccessResponse(remainderTime);
		}catch(Exception e) {
			log.error("获取过期时间异常",e);
			return ResponseResult.buildFailResponse();
		}

	}
	
	/**
	 * 取消消息回调
	 * @param bizId
	 * @return
	 */
	@RequestMapping(value = "/cancel-message" , method = RequestMethod.POST)
	public ResponseResult<String> cancelMessage(@RequestBody MessageDto message) {
		log.info("取消消息bizId={},module={}",message.getBizId(),message.getModule());
		if (null == message.getBizId() || StringUtils.isBlank(message.getModule())) {
			return ResponseResult.buildFailResponse(SysEnum.PARAM_VALIDATE_NULL.getCode(), SysEnum.PARAM_VALIDATE_NULL.getName());
		}
		if (StringUtils.isBlank(message.getSystem()) && "production".equals(message.getModule())) {
			message.setSystem("pos");
		}
		String incrMessageKey = RedisKeyUtil.getIncrMesssageKey(message.getSystem(),message.getModule(),message.getBizId());
		String incrMessageValue = RedisCache.get(incrMessageKey);
		if (StringUtils.isEmpty(incrMessageValue)) {
			return ResponseResult.buildFailResponse(SysEnum.MESSAGE_IS_EXPIRE.getCode(),SysEnum.MESSAGE_IS_EXPIRE.getName());
		}
		String msgId = RedisKeyUtil.getMessageId(message.getSystem(),message.getModule(),message.getBizId(), Long.valueOf(incrMessageValue));
		String consumeMessageKey = RedisKeyUtil.getConsumeMessageIdKey(msgId);
		RedisCache.delete(consumeMessageKey);
		return ResponseResult.buildSuccessResponse();
	}
	
	@RequestMapping(value = "/make-up-message" , method = RequestMethod.POST)
	public ResponseResult<?> makeUpMessage(@RequestBody MessageDto message) {
		log.info("补偿消息bizId={},system={},module{}=",message.getBizId(),message.getSystem(),message.getModule());
		if (null == message.getBizId() || StringUtils.isBlank(message.getModule())) {
			return ResponseResult.buildFailResponse(SysEnum.PARAM_VALIDATE_NULL.getCode(), SysEnum.PARAM_VALIDATE_NULL.getName());
		}
		if (StringUtils.isBlank(message.getSystem()) && "production".equals(message.getModule())) {
			message.setSystem("pos");
		}
		try{
			Map<String,Object> params = new HashMap<>();
			params.put("bizId", message.getBizId());
			params.put("status", 2);
			params.put("system", message.getSystem());
			params.put("module", message.getModule());
			List<DelayConsumeEvent> failDataList = dataService.getPostFailData(params);
			if (CollectionUtils.isNotEmpty(failDataList)) {
				for (DelayConsumeEvent consume :failDataList) {
	    			MessageDto messageDto = new MessageDto();
	    			messageDto.setBizId(consume.getBizId());
	    			messageDto.setSystem(consume.getSystem());
	    			messageDto.setModule(consume.getModule());
	    			messageDto.setCallbackUrl(consume.getCallbackUrl());
	    			messageDto.setBody(consume.getMessageBody());
	    			messageDto.setTtl(consume.getTtl());
	    			boolean result = callBackService.callBack(message);
	    			if (result) {
	    				dataService.updateStatusById(consume.getId());
	    			}
	    			dataService.updateSendNumById(consume.getId());
				}
				
			}
		}catch(Exception e) {
			log.error("补偿消息异常{}",e);
			return ResponseResult.buildFailResponse();
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	
	@RequestMapping(value = "/test" , method = RequestMethod.POST)
	public ResponseResult<String> test(@RequestBody MessageDto message) {
		TestDto t = new TestDto();
		t.setId(1600126078091264l);
		t.setOrderNo("O3101000118101000003");
		t.setOrderDate(new Date());
		message.setMsgId("1599112267694080l");
		message.setSystem("ecm");
		message.setModule("order");
		message.setCallbackUrl("http://www.baidu.com");
        message.setBody(JsonMapper.nonDefaultMapper().toJson(t));
        Long ttl = 6000l;
        message.setTtl(ttl);
		return delaySendService.sendDelayMessage(message);
	}
}
