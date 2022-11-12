package com.milisong.ecm.lunch.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.dto.CompanyBucketDto;
import com.milisong.ecm.lunch.dto.CompanyMealTimeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import java.util.List;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class CompanyConsumer {

	@StreamListener(MessageSink.MESSAGE_COMPANY_INPUT)
	public void acceptCompany(Message<String> message) {
		try {
			String msg = message.getPayload();
			log.info("company receive msg:{}", msg);
			if (msg == null) {
				return;
			}
			CompanyBucketDto companyBucketDto = JSONObject.parseObject(msg, CompanyBucketDto.class);
			if (companyBucketDto != null) {
				//接收MQ时，过滤配送时间信息，排序，放入redis
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
