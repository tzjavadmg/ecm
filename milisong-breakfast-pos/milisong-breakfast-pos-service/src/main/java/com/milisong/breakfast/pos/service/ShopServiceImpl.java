package com.milisong.breakfast.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.pos.api.ShopService;
import com.milisong.breakfast.pos.constant.ShopStatusEnum;
import com.milisong.breakfast.pos.domain.Shop;
import com.milisong.breakfast.pos.domain.ShopExample;
import com.milisong.breakfast.pos.dto.ShopDto;
import com.milisong.breakfast.pos.dto.ShopMqDto;
import com.milisong.breakfast.pos.mapper.ShopMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopMapper shopMapper;

	@Override
	public List<ShopDto> getShopList() {
		// 查询营业中的门店信息
		Map<String, Object> map = new HashMap<>();
		map.put("status", ShopStatusEnum.SHOP_STATUS_OPEN.getValue());
		List<ShopDto> list = shopMapper.getShopList(map);
		if (CollectionUtils.isNotEmpty(list)) {
			log.info("查询结果size={}", list.size());
		}
		return list;
	}

	@Override
	public void shopInfoByMq(String message) {
		 log.info("处理接收的shopMq message:{}",message);
		 try {
			ShopMqDto shopMqDto = JSONObject.parseObject(message,ShopMqDto.class);
			 Shop shop = shopMapper.selectByPrimaryKey(shopMqDto.getId());
			 if(null != shop) {
				 shop.setAddress(shopMqDto.getAddress());
				 shop.setIsDelete(shopMqDto.getIsDelete());
				 shop.setStatus(shopMqDto.getStatus());
				 shop.setName(shopMqDto.getName());
				 ShopExample shopExample = new ShopExample();
				 shopExample.createCriteria().andIdEqualTo(shop.getId());
				 shopMapper.updateByExampleSelective(shop, shopExample);
			 } else {
				 Shop record = BeanMapper.map(shopMqDto, Shop.class);
				 shopMapper.insertSelective(record);
			 }
		} catch (Exception e) {
			log.error("处理ShopMq消息出现异常{}",e);
		}
	}

}
