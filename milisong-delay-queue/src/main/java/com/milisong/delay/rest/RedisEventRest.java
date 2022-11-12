package com.milisong.delay.rest;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.delay.dto.MessageDto;
import com.milisong.delay.enums.StatusEnum;
import com.milisong.delay.enums.SysEnum;
import com.milisong.delay.service.DataService;
import com.milisong.delay.util.RedisKeyUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/v1/redis")
public class RedisEventRest {
	
	@Autowired
	private DataService dataService;
	
	
	/**
	 * 设置redis过期key
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "/set-expire-key" , method = RequestMethod.POST)
	public ResponseResult<String> getExpireKey(@RequestBody MessageDto message) {
		log.info("获取redis过期key{}",JSON.toJSONString(message));
		if (StringUtils.isBlank(message.getBody())||StringUtils.isBlank(message.getCallbackUrl())||
			null == message.getTtl()) {
			return ResponseResult.buildFailResponse(SysEnum.PARAM_VALIDATE_NULL.getCode(),SysEnum.PARAM_VALIDATE_NULL.getName());
		}
		byte status = StatusEnum.SUCCESS.getStatus();
		try{
			RedisCache.setEx(message.getBody(), "", message.getTtl(), TimeUnit.SECONDS);
			String keyPrefix = RedisKeyUtil.getBasisDataKey(message.getBody());
			//根据key存放回调地址，用于过期时通知业务系统
			RedisCache.set(keyPrefix, JSONObject.toJSONString(message));
		}catch(Exception e){
			status = StatusEnum.FAIL.getStatus();
			log.error("获取redis过期key异常",e);
		}
		dataService.insertProduction(message, status, 2);
		return ResponseResult.buildSuccessResponse(message.getBody());
	}
	
	/**
	 * 获取key过期所剩余的时间
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/get-expire-time" , method = RequestMethod.GET)
	public ResponseResult<Long> getExipreTime(@RequestParam(value = "key", required = true) String key) {
		log.info("获取redis过期时间{}",key);
		if (StringUtils.isBlank(key)) {
			return ResponseResult.buildFailResponse(SysEnum.PARAM_VALIDATE_NULL.getCode(),SysEnum.PARAM_VALIDATE_NULL.getName());
		}
		long expireTime = RedisCache.getExpire(key, TimeUnit.SECONDS);
		return ResponseResult.buildSuccessResponse(expireTime);
	}
	
	@RequestMapping(value = "/pause-key" , method = RequestMethod.GET)
	public ResponseResult<Long> pauseKey(@RequestParam(value = "key", required = true) String key) {
		log.info("暂停redis key{}",key);
		if (StringUtils.isBlank(key)) {
			return ResponseResult.buildFailResponse(SysEnum.PARAM_VALIDATE_NULL.getCode(),SysEnum.PARAM_VALIDATE_NULL.getName());
		}
		long expireTime = RedisCache.getExpire(key, TimeUnit.SECONDS);
		if (expireTime == -2) {
			expireTime = 0l;
		}else {
			RedisCache.delete(key);
		}
		return ResponseResult.buildSuccessResponse(expireTime);
	}
}
