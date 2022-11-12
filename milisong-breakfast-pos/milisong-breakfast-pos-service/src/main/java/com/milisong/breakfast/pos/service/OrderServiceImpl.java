package com.milisong.breakfast.pos.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.milisong.breakfast.pos.dto.OrderDeliveryGoodsMessage;
import com.milisong.breakfast.pos.dto.OrderDeliveryMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.milisong.breakfast.pos.api.OrderService;
import com.milisong.breakfast.pos.constant.GoodsSummaryEnum;
import com.milisong.breakfast.pos.constant.RedisKeyConstant;
import com.milisong.breakfast.pos.domain.GoodsSummary;
import com.milisong.breakfast.pos.domain.GoodsSummaryExample;
import com.milisong.breakfast.pos.domain.Shop;
import com.milisong.breakfast.pos.domain.ShopExample;
import com.milisong.breakfast.pos.dto.OrderRefundDto;
import com.milisong.breakfast.pos.dto.OrderStatusChangeDto;
import com.milisong.breakfast.pos.mapper.BaseGoodsSummaryMapper;
import com.milisong.breakfast.pos.mapper.BaseShopMapper;

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
	@Resource
	private BaseGoodsSummaryMapper goodsSummaryMapper;
	
	
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
			
			if(jsonObject.getByteValue("orderType") == 1) {
				log.warn("该订单为午餐订单，忽略");
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

		insertGoodsSummary(list);
	}

    private void insertGoodsSummary(JSONArray list) {
        List<OrderDeliveryMessage> orderDtos = JSONArray.parseArray(list.toJSONString(), OrderDeliveryMessage.class);
        for (OrderDeliveryMessage orderDto : orderDtos) {
            insertGoodsSummary(orderDto);
        }

    }

    @Override
	public void refoundOrder(JSONArray list) {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
		Integer orderType = list.getJSONObject(0).getInteger("orderType");
		if (null == orderType || orderType == 1) {
			log.warn("类型为午餐 忽略！");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonObject =	list.getJSONObject(i);
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
		updateGoodsSummary(list);

	}

	/**
	 * 储存商品汇总信息
	 * @param dto
	 */
	private void insertGoodsSummary(OrderDeliveryMessage dto){
		RLock lock = LockProvider.getLock("MQ_GOODSUMMARY" + dto.getDeliveryNo());
		try {
			GoodsSummaryExample example = new GoodsSummaryExample();
			example.createCriteria().andOrderNoEqualTo(dto.getDeliveryNo());
			List<GoodsSummary> goodsSummaries = goodsSummaryMapper.selectByExample(example);
			if (goodsSummaries.isEmpty()) {
				List<OrderDeliveryGoodsMessage> orderList = dto.getDeliveryGoods();
				Shop shop = getShopByCode(dto.getShopCode());
				doInsertGoodsSummaries(dto, orderList, shop);
			}
		} finally {
			lock.unlock();
		}

	}

	private void doInsertGoodsSummaries(OrderDeliveryMessage dto, List<OrderDeliveryGoodsMessage> orderList, Shop shop) {
		if (!orderList.isEmpty()) {
			orderList.stream().forEach(orderDetail ->{
				if (!orderDetail.getIsCombo()) {
					GoodsSummary goodsSummary = new GoodsSummary();
					goodsSummary.setId(IdGenerator.nextId());
					goodsSummary.setGoodsName(orderDetail.getGoodsName());
					goodsSummary.setGoodsCode(orderDetail.getGoodsCode());
					goodsSummary.setShopCode(dto.getShopCode());
					goodsSummary.setDeliveryDate(dto.getDeliveryTimezoneTo());
					goodsSummary.setDeliveryTime(dto.getDeliveryTimezoneTo());
					goodsSummary.setReservedDate(dto.getOrderDate());
					goodsSummary.setShopName(shop.getName());
					goodsSummary.setShopId(shop.getId());
					goodsSummary.setReservedCount(orderDetail.getGoodsCount());
					goodsSummary.setOrderNo(dto.getDeliveryNo());
					goodsSummary.setStatus(GoodsSummaryEnum.NORMAL.getValue());
					goodsSummaryMapper.insertSelective(goodsSummary);
				} else {
					// 若为套餐，
					doInsertGoodsSummaries(dto,orderDetail.getComboDetails(),shop);
				}
		});
		} else {
			log.warn("没有订单数据！");
		}
	}

	@Override
	public void orderStatusChange(String msg) {
        OrderStatusChangeDto changeDto = JSONObject.parseObject(msg, OrderStatusChangeDto.class);
		String orderNo = changeDto.getOrderNo();
		if (orderNo.startsWith("L")){
			log.warn("类型为午餐 忽略！");
			return ;
		}
        GoodsSummaryExample example = new GoodsSummaryExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<GoodsSummary> goodsSummaries = goodsSummaryMapper.selectByExample(example);
        if (goodsSummaries.isEmpty()) {
            log.error("没找到商品汇总信息,订单编号:{}", orderNo);
            return;
        }
        GoodsSummary goodsSummary = goodsSummaries.get(0);
        goodsSummary.setDeliveryDate(changeDto.getDate());
        goodsSummaryMapper.updateByPrimaryKeySelective(goodsSummary);
    }

	/**
	 * 退款时更新商品汇总表
	 * @param list
	 */
	private void updateGoodsSummary(JSONArray list) {
		List<OrderRefundDto> orderRefundDtos = JSONArray.parseArray(list.toJSONString(), OrderRefundDto.class);
		
		GoodsSummary record = new GoodsSummary();
		record.setStatus(GoodsSummaryEnum.REFUND.getValue());

        List<String> orderNoList = orderRefundDtos.stream().map(item -> item.getDeliveryNo()).collect(Collectors.toList());

        GoodsSummaryExample example = new GoodsSummaryExample();
        example.createCriteria().andOrderNoIn(orderNoList);

        goodsSummaryMapper.updateByExampleSelective(record, example);
		
	}
}
