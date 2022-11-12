package com.milisong.ecm.lunch.mq;

import java.util.List;
import java.util.Map;

import com.milisong.ecm.common.enums.ConfigItemEnum;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.goods.dto.BannerConfigDto;
import com.milisong.ecm.lunch.goods.dto.InterceptConfigDto;
import com.milisong.ecm.lunch.goods.dto.SingleConfigDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 配置信息消费者
 *
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class ConfigConsumer {

	@Value("${shop.default-code}")
	private String SHOP_DEFAULT_CODE;

	/**
	 * 接收全局、单个门店配置消息
	 * @param message
	 */
	@StreamListener(MessageSink.MESSAGE_CONFIG_INPUT)
	public void accept(Message<String> message) {
		log.info("接收全局、单个门店配置信息，参数={}", JSONObject.toJSONString(message));
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("receive msg:{}", msg);
			if (msg == null) {
				return;
			}

			Map<String, Object> map = JSON.parseObject(msg);
			String type = map.get("type").toString();
			String data = JsonMapper.nonDefaultMapper().toJson(map.get("data"));
			switch (type) {
			case "global_input":
				this.global(data);
				break;
			case "single_input":
				this.single(data);
				break;
			default:
				break;
			}

		} catch (Exception e) {
			log.error("接收接收全局、单个门店消息结束，出现异常", e);
		}
	}
	
	
	/**
	 * 接收门店截单配置消息
	 * @param message
	 */
	@StreamListener(MessageSink.MESSAGE_CONFIG_INTERCEPT_INPUT)
	public void acceptIntercept(Message<String> message) {
		log.info("接收门店截单配置信息，参数={}", JSONObject.toJSONString(message));
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("receive msg:{}", msg);
			if (msg == null) {
				return;
			}

			Map<String, Object> map = JSON.parseObject(msg);
 			String data = JsonMapper.nonDefaultMapper().toJson(map.get("data"));
 			intercept(data);
		} catch (Exception e) {
			log.error("接收门店截单消息结束，出现异常", e);
		}
	}
	@StreamListener(MessageSink.MESSAGE_CONFIG_BANNER_INPUT)
	public void acceptBanner(Message<String> message) {
		log.info("接收banner图配置信息，参数={}", JSONObject.toJSONString(message));
		try {
			String messageHeadersId = null;
			if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
				messageHeadersId = message.getHeaders().getId().toString();
			}
			if (messageHeadersId == null) {
				log.info("接收消息结束，messageHeadersId=null");
				return;
			}

			String msg = message.getPayload();
			log.info("receive msg:{}", msg);
			if (msg == null) {
				return;
			}

			Map<String, Object> map = JSON.parseObject(msg);
 			String data = JsonMapper.nonDefaultMapper().toJson(map.get("data"));
 			banner(data);
		} catch (Exception e) {
			log.error("接收banner图消息结束，出现异常", e);
		}
	}
	
	
	

	private void global(String data) {
		log.info("全局配置信息，参数={}", JSONObject.toJSONString(data));
		Map<String, Object> map = JSON.parseObject(data);
		String globalConfigKey = RedisKeyUtils.getGlobalConfigKey();
		for(Map.Entry<String, Object> result :map.entrySet()) {
			RedisCache.hPut(globalConfigKey, result.getKey(), result.getValue());
		}
	}

	private void banner(String data) {
		List<BannerConfigDto> bannerList = JSONObject.parseArray(data, BannerConfigDto.class);
		if (CollectionUtils.isNotEmpty(bannerList)) {
			// 按门店维度
			String shopConfigKey = RedisKeyUtils.getShopConfigKey(bannerList.get(0).getShopCode());
			RedisCache.hPut(shopConfigKey, ConfigItemEnum.BANNER.getValue(), JSON.toJSONString(bannerList));

			// 全局配置，保证在shopinfo接口没有拿到shopcode时候，能返回banner图信息
			if (bannerList.get(0).getShopCode().equals(SHOP_DEFAULT_CODE)) {
				String configKey = RedisKeyUtils.getGlobalConfigKey();
				RedisCache.hPut(configKey, ConfigItemEnum.BANNER.getValue(), JSON.toJSONString(bannerList));
			}
		}
	}

	public void intercept(String data) {
		List<InterceptConfigDto> interceptList = JSONObject.parseArray(data, InterceptConfigDto.class);
		if (CollectionUtils.isNotEmpty(interceptList)) {
			// 按门店维度
			String shopConfigKey = RedisKeyUtils.getShopConfigKey(interceptList.get(0).getShopCode());
			RedisCache.hPut(shopConfigKey, ConfigItemEnum.DELIVERY_TIMEZONE.getValue(), JSON.toJSONString(interceptList));

			// 全局配置，保证在shopinfo接口没有拿到shopcode时候返回默认信息
			if (interceptList.get(0).getShopCode().equals(SHOP_DEFAULT_CODE)) {
				String configKey = RedisKeyUtils.getGlobalConfigKey();
				RedisCache.hPut(configKey, ConfigItemEnum.DELIVERY_TIMEZONE.getValue(), JSON.toJSONString(interceptList));
			}
		}
	}

	public void single(String data) {
		log.info("门店配置信息，参数={}", JSONObject.toJSONString(data));
		List<SingleConfigDto> singleList = JSONObject.parseArray(data, SingleConfigDto.class);
		if (CollectionUtils.isNotEmpty(singleList)) {
			for (SingleConfigDto single : singleList) {
				String shopConfigKey = RedisKeyUtils.getShopConfigKey(single.getShopCode());
				String configValue = single.getConfigValue();
				RedisCache.hPut(shopConfigKey, single.getConfigKey(), configValue);

				// 全局配置，保证在shopinfo接口没有拿到shopcode时候返回默认信息
				if (single.getShopCode().equals(SHOP_DEFAULT_CODE)) {
					String configKey = RedisKeyUtils.getGlobalConfigKey();
					RedisCache.hPut(configKey, single.getConfigKey(), single.getConfigValue());
				}
			}
		}
	}
}
