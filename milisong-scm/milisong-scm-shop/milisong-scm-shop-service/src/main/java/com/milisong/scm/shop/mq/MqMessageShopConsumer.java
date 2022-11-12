package com.milisong.scm.shop.mq;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.scm.shop.api.ConfigService;
import com.milisong.scm.shop.constant.ConfigConstant;
import com.milisong.scm.shop.dto.config.*;
import com.milisong.scm.shop.util.RedisConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSONObject;
import com.milisong.scm.shop.api.BuildingApplyService;
import com.milisong.scm.shop.api.BuildingService;
import com.milisong.scm.shop.dto.building.BuildingApplyDto;
import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.scm.shop.dto.building.CompanyDto;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@EnableBinding(MqMessageShopSource.class)
public class MqMessageShopConsumer {
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private BuildingApplyService buildingApplyService;
	@Autowired
	private ConfigService configService;

	private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	

	/**
	 * 楼宇申请
	 * @param message
	 */
	@StreamListener(MqMessageShopSource.BUILDING_APPLY)
	public void buildingApply(Message<String> message) {
		log.info("开始消费C端的楼宇申请信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			BuildingApplyDto dto = JSONObject.parseObject(payload, BuildingApplyDto.class);
			buildingApplyService.save(dto);
		} else {
			log.warn("收到C端的楼宇申请信息为空！！！");
		}
	}

	@StreamListener(value = MqMessageShopSource.CONFIG_INPUT)
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

	@StreamListener(value = MqMessageShopSource.CONFIG_INTERCEPT_INPUT)
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
					dto.setDispatchTime(format.parse(o.getDispatchTime()));
					dto.setDeliveryTimeBegin(format.parse(o.getStartTime()));
					dto.setDeliveryTimeEnd(format.parse(o.getEndTime()));
					dto.setOrderInterceptTime(format.parse(o.getCutOffTime()));
					dto.setShopCode(o.getShopCode());
					dto.setShopName(o.getShopName());
					dto.setFirstOrdersetTime(format.parse(o.getFirstOrdersetTime()));
					dto.setSecondOrdersetTime(format.parse(o.getSecondOrdersetTime()));
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
	@StreamListener(value = MqMessageShopSource.SHOP_CREATE_INPUT)
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
