package com.milisong.ecm.lunch.mq;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.goods.dto.GoodsScheduleMqDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class ScheduleConsumer {
	@StreamListener(MessageSink.MESSAGE_SCHEDULE_INPUT)
	public void acceptMovie(Message<String> message) {
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收档期消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("movie receive msg:{}", msg);
			if (msg == null) {
				return;
			}
			
			List<GoodsScheduleMqDto> scheduleList = JSONArray.parseArray(msg, GoodsScheduleMqDto.class);
			if (CollectionUtils.isNotEmpty(scheduleList)) {
				for (GoodsScheduleMqDto schedule : scheduleList) {
					String shopMovieDateKey = RedisKeyUtils.getShopScheduleDateKey(schedule.getShopCode());
		        	RedisCache.hPut(shopMovieDateKey,DateFormatUtils.format(schedule.getScheduleDate(), "yyyy-MM-dd"), JSON.toJSONString(schedule));

				}
			}
		} catch (Exception e) {
			log.error("接收档期消息结束，出现异常", e);
		}
	}
}
