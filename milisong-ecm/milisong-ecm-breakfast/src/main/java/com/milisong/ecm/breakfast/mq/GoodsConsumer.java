package com.milisong.ecm.breakfast.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.breakfast.dto.GoodsMqDto;
import com.milisong.ecm.common.util.RedisKeyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class GoodsConsumer {
	@StreamListener(MessageSink.MESSAGE_GOODS_BASIC)
	public void accept(Message<String> message) {
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收商品消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("goods receive msg:{}", msg);
			if (msg == null) {
				return;
			}
			GoodsMqDto goodsMqDto = JSONObject.parseObject(msg, GoodsMqDto.class);
			if (goodsMqDto != null) {
				String goodsBasicKey = RedisKeyUtils.getGoodsBasicKey(goodsMqDto.getCode());
	            RedisCache.set(goodsBasicKey, msg);
			}
		} catch (Exception e) {
			log.error("接收商品消息结束，出现异常", e);
		}
	}
}
