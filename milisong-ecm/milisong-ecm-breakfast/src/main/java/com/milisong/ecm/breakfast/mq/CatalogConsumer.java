package com.milisong.ecm.breakfast.mq;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.breakfast.dto.CatalogMqDto;
import com.milisong.ecm.common.util.RedisKeyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class CatalogConsumer {
	
	@StreamListener(MessageSink.MESSAGE_CATALOG_INPUT)
	public void acceptCatalog(Message<String> message) {
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收类目消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("catalog receive msg:{}", msg);
			if (msg == null) {
				return;
			}
			List<CatalogMqDto> catalogList  =JSONArray.parseArray(msg, CatalogMqDto.class);
			if (CollectionUtils.isNotEmpty(catalogList)) {
				for (CatalogMqDto catalog : catalogList) {
					String catalogBasicKey = RedisKeyUtils.getCatglogBasicKey();
					RedisCache.hPut(catalogBasicKey,catalog.getCode(), JSON.toJSONString(catalog));
				}
			}
		} catch (Exception e) {
			log.error("接收类目消息结束，出现异常", e);
		}
	}
}
