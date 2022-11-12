package com.milisong.delay.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.delay.domain.DelayConsumeEvent;
import com.milisong.delay.dto.MessageDto;
import com.milisong.delay.mapper.DelayConsumeEventMapper;
import com.milisong.delay.util.RedisKeyUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Configurable
@EnableScheduling 
public class DelayTask {
	
	@Autowired
	private DelayConsumeEventMapper consumeEventMapper;
	@Autowired
	private RestTemplate restTemplate;
	
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void selectSendFailData() {
    	log.info("查询回调失败的请求");
    	String key = RedisKeyUtil.getIncrTaskKey();
    	try{
    		long incr = RedisCache.incrBy(key, 1);
    		if (incr == 1) {
            	List<DelayConsumeEvent> consumeList = consumeEventMapper.selectFailData();
            	if (CollectionUtils.isNotEmpty(consumeList)) {
            		for (DelayConsumeEvent consume : consumeList) {
            			MessageDto message = new MessageDto();
            			message.setBizId(consume.getBizId());
            			message.setSystem(consume.getSystem());
            			message.setModule(consume.getModule());
            			message.setCallbackUrl(consume.getCallbackUrl());
            			message.setBody(consume.getMessageBody());
            			message.setTtl(consume.getTtl());
            			try{
                			ResponseEntity<String> responseResult = restTemplate.postForEntity("http://localhost:8084/api/order/test", message, String.class);
                			if (responseResult.getStatusCode() == HttpStatus.OK) {
                				ResponseResult<String> result = JSONObject.parseObject(responseResult.getBody(), ResponseResult.class) ;
                				if (result.isSuccess()) {
                					//更新发送状态为成功
                					consumeEventMapper.updateStatusById(consume.getId());
                				}
                			} 
            			}catch(Exception e){
            				log.error("定时任务回调业务系统异常",e);
            			}
            			//更新回调次数
            			consumeEventMapper.updateSendNumById(consume.getId());

            		}
            	}
    		}
    	}catch(Exception e) {
    		log.error("定时任务回调失败请求异常",e);
    	}finally {
    		RedisCache.delete(key);
    	}

    }

}
