package com.milisong.scm.base.mq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSONObject;
import com.milisong.scm.base.service.OperationLogProcessService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MqMessageSource.class)
public class MqMessageConsumer {
	@Autowired
	private OperationLogProcessService operationLogProcessService;
	
	/**
	 * 操作日志消费
	 * @param message
	 */
	@StreamListener(MqMessageSource.OPERATION_LOG_INPUT)
	public void operationLog(Message<String> message) {
		log.info("开始消费操作日志信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			this.operationLogProcessService.saveLog(payload);
		} else {
			log.warn("收到操作日志的空消息！！！");
		}
	}
}
