package com.milisong.ecm.breakfast.mq;


import com.alibaba.fastjson.JSON;
import com.milisong.ecm.breakfast.dto.CompanyMealTimeDto;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.breakfast.dto.CompanyBucketDto;
import com.milisong.ecm.common.util.RedisKeyUtils;

import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class CompanyConsumer {
	
	@StreamListener(MessageSink.MESSAGE_COMPANY_INPUT)
	public void acceptCompany(Message<String> message) {
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收公司消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("company receive msg:{}", msg);
			if (msg == null) {
				return;
			}
			CompanyBucketDto companyBucketDto = JSONObject.parseObject(msg, CompanyBucketDto.class);
			if (companyBucketDto != null) {
			    //接收MQ时，过滤配送时间信息，排序，放入redis
				List<CompanyMealTimeDto> mealTimeList = companyBucketDto.getMealTimeList();
				if(mealTimeList!=null && mealTimeList.size() > 0){
					mealTimeList.removeIf(o->o.getIsDeleted()
							|| o.getDeliveryTimeBegin() ==null
							|| o.getDeliveryTimeEnd() == null);
					mealTimeList.sort((o1,o2)-> (int) (o1.getDeliveryTimeBegin().getTime()-o2.getDeliveryTimeEnd().getTime()));
				}
				List<CompanyMealTimeDto> lunchMealTimeList = companyBucketDto.getLunchMealTimeList();
				if(lunchMealTimeList!=null && lunchMealTimeList.size() > 0){
					lunchMealTimeList.removeIf(o->o.getIsDeleted()
							|| o.getDeliveryTimeBegin() ==null
							|| o.getDeliveryTimeEnd() == null);
					lunchMealTimeList.sort((o1,o2)-> (int) (o1.getDeliveryTimeBegin().getTime()-o2.getDeliveryTimeBegin().getTime()));
				}
				//维护按公司维度信息
				String companyKey = RedisKeyUtils.getCompanyKey(companyBucketDto.getId());
	            RedisCache.set(companyKey, msg);
	            
	            //维护公司集合信息
	            String companyListKey = RedisKeyUtils.getCompanyListKey();
	        	RedisCache.hPut(companyListKey,companyBucketDto.getId().toString(), JSON.toJSONString(companyBucketDto));
			}
		} catch (Exception e) {
			log.error("接收公司消息结束，出现异常", e);
		}
	}
}
