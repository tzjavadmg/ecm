package com.milisong.pos.production.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.pos.production.api.OrderService;
import com.milisong.pos.production.constant.RedisKeyConstant;
import com.milisong.pos.production.domain.Shop;
import com.milisong.pos.production.domain.ShopExample;
import com.milisong.pos.production.dto.OrderDetailDto;
import com.milisong.pos.production.dto.OrderDto;
import com.milisong.pos.production.dto.OrderRefundDto;
import com.milisong.pos.production.dto.RefundGoodsMessage;
import com.milisong.pos.production.mapper.BaseShopMapper;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年10月30日---上午11:35:22
*
*/
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	BaseShopMapper baseShopMapper;
	
	
	public Shop getShopByCode(String shopCode) {
		Shop shop = new Shop();
		if (StringUtils.isNotBlank(shopCode)) {
			ShopExample example = new ShopExample();
			  example.createCriteria() .andCodeEqualTo(shopCode) .andIsDeleteEqualTo(false);
			List<Shop> list = baseShopMapper.selectByExample(example);
			if (CollectionUtils.isNotEmpty(list) && list.get(0) != null) {
				shop = list.get(0);
			}
		}
		return shop;
	}
	@Override
	public void saveOrder(JSONArray list) {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
		
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonObject =	list.getJSONObject(i);
			
			if(jsonObject.getByteValue("orderType") == 0) {
				log.warn("该订单为早餐订单，忽略");
				return;
			}
			
			JSONArray  jsonDetailsArray = jsonObject.getJSONArray("deliveryGoods");
			String shopCode =jsonObject.get("shopCode").toString();
			Date deliveryDate = jsonObject.getDate("deliveryTimezoneFrom");
			String date = simp.format(deliveryDate);
			Shop shop  = getShopByCode(shopCode);
			for (int j = 0; j < jsonDetailsArray.size(); j++) {
				JSONObject jsonObjectDetail = jsonDetailsArray.getJSONObject(j);
				String goodsCode = jsonObjectDetail.get("goodsCode").toString();
				String key = RedisKeyConstant.getShopGoodsSaleCount(shop.getId(),date,goodsCode);
				RedisCache.incrBy(key, jsonObjectDetail.getInteger("goodsCount"));
				RedisCache.expire(key, 30, TimeUnit.DAYS);
			}
		}
	}

	@Override
	public void refoundOrder(JSONArray list) {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonObject =	list.getJSONObject(i);
			Integer orderType = jsonObject.getInteger("orderType");
			log.info("判断如果不是午餐就不处理 orderType{}",orderType);
			if(1 !=orderType) return ;
			JSONArray  jsonDetailsArray = jsonObject.getJSONArray("refundGoods");
			String shopCode =jsonObject.get("shopCode").toString();
			Shop shop  = getShopByCode(shopCode);
			String date = simp.format(jsonObject.getDate("deliveryDate"));
			for (int j = 0; j < jsonDetailsArray.size(); j++) {
				JSONObject jsonObjectDetail = jsonDetailsArray.getJSONObject(j);
				String goodsCode = jsonObjectDetail.get("goodsCode").toString();
				String key = RedisKeyConstant.getShopGoodsSaleCount(shop.getId(),date,goodsCode);
				RedisCache.incrBy(key, -jsonObjectDetail.getInteger("goodsCount"));
				RedisCache.expire(key, 30, TimeUnit.DAYS);
			}
		}
	}
	
	
	@Override
	public void saveOrder(List<OrderDto> list) {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
		String date = simp.format(new Date());
		String shopCode = list.get(0).getShopCode();
		Shop shop  = getShopByCode(shopCode);
		//遍历所有信息、
		for (OrderDto orderDto : list) {
			//获取门店信息
			shopCode = orderDto.getShopCode();
			List<OrderDetailDto> listOrderDetail =	orderDto.getOrderDetails();
			for (OrderDetailDto orderDetailDto : listOrderDetail) {
				//获取rediskey
				String key = RedisKeyConstant.getShopGoodsSaleCount(shop.getId(),date,orderDetailDto.getGoodsCode());
				RedisCache.incrBy(key, orderDetailDto.getGoodsCount());
				RedisCache.expire(key, 1, TimeUnit.DAYS);
			}
		}

	}

	@Override
	public void refoundOrder(List<OrderRefundDto> list) {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
		String date = simp.format(new Date());
		String shopCode = list.get(0).getShopCode();
		Shop shop  = getShopByCode(shopCode);
		for (OrderRefundDto orderRefundDto : list) {
			shopCode = orderRefundDto.getShopCode();
			List<RefundGoodsMessage> listGoods = orderRefundDto.getRefundGoods();
			for (RefundGoodsMessage refundGoodsMessage : listGoods) {
				String key = RedisKeyConstant.getShopGoodsSaleCount(shop.getId(),date,refundGoodsMessage.getGoodsCode());
				RedisCache.incrBy(key, -Integer.parseInt(refundGoodsMessage.getGoodsCount()));
				RedisCache.expire(key, 1, TimeUnit.DAYS);
			}
		}
	}
}
