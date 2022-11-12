/*
package com.milisong.ecm.lunch.mq;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@EnableBinding(MessageSource.class)
@Component
public class CompanyProducer {

	@Resource
	@Output(MessageSource.MESSAGE_COMPANY_OUTPUT)
	private MessageChannel companyChannel;
	
	public void sendCompanyInfo (Object obj) {
		String msg = JSONObject.toJSONString(obj);
		log.info("发送收货地址给scm{}",msg);
		companyChannel.send(MessageBuilder.withPayload(msg).build());
	}

}
*/
