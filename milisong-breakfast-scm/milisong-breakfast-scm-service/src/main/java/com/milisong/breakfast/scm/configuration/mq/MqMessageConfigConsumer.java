package com.milisong.breakfast.scm.configuration.mq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.configuration.api.ConfigService;
import com.milisong.breakfast.scm.configuration.constant.ConfigConstant;
import com.milisong.breakfast.scm.configuration.dto.ConfigDto;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigDto;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigMQDto;
import com.milisong.breakfast.scm.configuration.dto.ShopSingleConfigDto;
import com.milisong.breakfast.scm.configuration.util.RedisConfigUtil;
import com.milisong.scm.base.dto.mq.ShopCreateMqDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MqMessageConfigSource.class)
public class MqMessageConfigConsumer {

	@Autowired
	private ConfigService configService;


	private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

	@StreamListener(value = MqMessageConfigSource.CONFIG_INPUT)
	public void configConsume(Message<String> message) {
		log.info("消费配置信息，参数：{}", message.getPayload());
		String payload = message.getPayload();
		ConfigDto<JSONObject> configDto = JSON.parseObject(payload,ConfigDto.class);
		if(configDto.getType().equals(ConfigConstant.GLOBAL_CONFIG.getValue())){
			//更新全局配置
			HashMap<String,String> hashMap = JSONObject.parseObject(JSON.toJSONString(configDto.getData()),HashMap.class);
			RedisConfigUtil.cacheGlobalConfig(hashMap,configDto.getOperation());
		}else if(configDto.getType().equals(ConfigConstant.SHOP_SINGLE_CONFIG.getValue())){
			//更新门店属性配置
			List<ShopSingleConfigDto> list = BeanMapper.mapList((List)configDto.getData(), ShopSingleConfigDto.class);
			RedisConfigUtil.cacheShopSingleConfig(list,configDto.getOperation());
		}
	}

	@StreamListener(value = MqMessageConfigSource.CONFIG_INTERCEPT_INPUT)
	public void configInterceptConsume(Message<String> message) {
		log.info("消费截单配置信息，参数：{}", message.getPayload());
		try {
			String payload = message.getPayload();
			ConfigDto<Object> configDto = JSON.parseObject(payload,ConfigDto.class);
			if(configDto.getType().equals(ConfigConstant.INTERCEPT_CONFIG.getValue())){
				List<ShopInterceptConfigMQDto> list = BeanMapper.mapList((List)configDto.getData(), ShopInterceptConfigMQDto.class);
				Long shopId = list.get(0).getShopId();
				List<ShopInterceptConfigDto> arrayList = new ArrayList<>();
				for (ShopInterceptConfigMQDto o:list) {
					ShopInterceptConfigDto dto = new ShopInterceptConfigDto();
					dto.setId(o.getId());
					dto.setShopId(o.getShopId());
					dto.setDeliveryTimeBegin(format.parse(o.getStartTime()));
					dto.setDeliveryTimeEnd(format.parse(o.getEndTime()));
					dto.setOrderInterceptTime(format.parse(o.getCutOffTime()));
					dto.setShopCode(o.getShopCode());
					dto.setShopName(o.getShopName());
					dto.setFirstOrdersetTime(format.parse(o.getFirstOrdersetTime()));
					if(o.getDispatchTime()!=null){
						dto.setDispatchTime(format.parse(o.getDispatchTime()));
					}
					//dto.setSecondOrdersetTime(format.parse(o.getSecondOrdersetTime()));
					dto.setIsDefault(o.getIsDefault());
					dto.setMaxOutput(o.getMaxCapacity());
					arrayList.add(dto);
				}
				RedisConfigUtil.cacheShopIntercept(shopId,arrayList);
			}
		} catch (Exception e) {
			log.error("消费门店截单配置MQ信息失败---",e);
		}
	}

	/**
	 * 新增门店，拷贝门店属性配置
	 * @param message
	 */
	@StreamListener(value = MqMessageConfigSource.SHOP_CREATE_INPUT)
	public void shopSaveConsume(Message<String> message){
		log.info("消费门店新增信息，参数：{}", message.getPayload());
		try {
			String payload = message.getPayload();
			ShopReqDto reqDto = JSON.parseObject(payload, ShopReqDto.class);
			configService.copyConfig(reqDto.getSrcId(),reqDto);
			configService.syncShopConfig(reqDto.getId());
		}catch (Exception e){
			log.error("消费门店新增信息异常---",e);
		}
	}

}
