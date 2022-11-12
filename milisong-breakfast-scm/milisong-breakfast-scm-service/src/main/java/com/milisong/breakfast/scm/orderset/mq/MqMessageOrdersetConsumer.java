package com.milisong.breakfast.scm.orderset.mq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSONObject;
import com.milisong.breakfast.scm.orderset.api.OrderSetService;
import com.milisong.dms.dto.shunfeng.DeliveryOrderMqDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MqMessageOrdersetSource.class)
public class MqMessageOrdersetConsumer {
	@Autowired
	private OrderSetService orderSetService;
	
//	/**
//	 * 顺丰订单状态变更消费
//	 * @param message
//	 */
//	@StreamListener(MqMessageOrdersetSource.SF_STATUS_CHANGE_INPUT)
//	public void sfStatusChange(Message<String> message) {
//		log.info("开始消费顺丰订单状态变更信息，参数：{}", JSONObject.toJSONString(message));
//		String payload = message.getPayload();
//		if(StringUtils.isNotBlank(payload)) {
//			DeliveryOrderMqDto dto = JSONObject.parseObject(payload, DeliveryOrderMqDto.class);
//			orderSetService.updateDistributionStatus(dto);
//		} else {
//			log.warn("收到顺丰订单状态变更的空消息！！！");
//		}
//	}
}
