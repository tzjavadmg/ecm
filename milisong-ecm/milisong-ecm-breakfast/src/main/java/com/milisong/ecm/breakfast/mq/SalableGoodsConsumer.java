package com.milisong.ecm.breakfast.mq;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.google.common.collect.Maps;
import com.milisong.ecm.common.util.RedisKeyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class SalableGoodsConsumer {
	
	@StreamListener(MessageSink.MESSAGE_SALABLE_GOODS)
	public void acceptMovie(Message<String> message) {
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收可售商品消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("salableGoods receive msg:{}", msg);
			if (msg == null) {
				return;
			}
			Map<String,Map<Long,List<String>>> resultMap = jsonToMap(msg);
			if (resultMap != null && resultMap.size() > 0 ) {
				for (Map.Entry<String, Map<Long,List<String>>> map : resultMap.entrySet()) {
					String shopCode = map.getKey();
					Map<Long,List<String>> value = map.getValue();
					for (Map.Entry<Long, List<String>> cataMap : value.entrySet()) {
						Long dateMillis = cataMap.getKey();
						List<String> goodsCataList = cataMap.getValue();
						Date date = new Date(dateMillis);
						String dailyShopGoodsKey = RedisKeyUtils.getDailyShopGoodsKey(shopCode, date);
			            RedisCache.set(dailyShopGoodsKey, JSON.toJSONString(goodsCataList));
			            RedisCache.expireAt(dailyShopGoodsKey, DateUtils.addDays(date, 1));
					}
				}
			}
			
		} catch (Exception e) {
			log.error("接收可售商品消息结束，出现异常", e);
		}
	}
	
	
	public Map<String,Map<Long,List<String>>> jsonToMap(String msg) {
		Map<String,Map<Long,List<String>>> resultMap = Maps.newHashMap();
		Map<Long,List<String>> dateMap = Maps.newHashMap();
		Map<String,Map<Long,List<String>>> map = (Map) JSONObject.parse(msg);
		for (Map.Entry<String, Map<Long,List<String>>> result : map.entrySet()) {
			String shopCode = result.getKey();
			Map<Long,List<String>> goodsMap = result.getValue();
			for (Map.Entry<Long, List<String>> goods : goodsMap.entrySet()) {
				Long salesDate = goods.getKey();
				List<String> goodsCodeList = goods.getValue();
				dateMap.put(salesDate, goodsCodeList);
			}
			resultMap.put(shopCode, dateMap);
		}
		return resultMap;
		
		
	}
}
