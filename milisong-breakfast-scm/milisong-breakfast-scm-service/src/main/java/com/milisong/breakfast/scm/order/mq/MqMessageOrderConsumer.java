package com.milisong.breakfast.scm.order.mq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.milisong.breakfast.scm.order.api.OrderService;
import com.milisong.breakfast.scm.order.constant.OrderStatusEnum;
import com.milisong.breakfast.scm.order.constant.OrderTypeEnum;
import com.milisong.breakfast.scm.order.dto.mq.OrderDetailDto;
import com.milisong.breakfast.scm.order.dto.mq.OrderDto;
import com.milisong.breakfast.scm.order.dto.mq.OrderRefundDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MqMessageOrderSource.class)
public class MqMessageOrderConsumer {
	@Autowired
	@Qualifier(value="orderService")
	private OrderService orderService;
	
	/**
	 * 订单消费
	 * @param message
	 */
	@StreamListener(MqMessageOrderSource.ORDER_INPUT)
	public void order(Message<String> message) {
		log.info("开始消费订单信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			try {
				List<OrderDeliveryMessage> nwwList = JSONObject.parseArray(payload, OrderDeliveryMessage.class);
				List<OrderDto> list = convertOrderDto(nwwList);
	
				if(CollectionUtils.isEmpty(list)) {
					log.warn("订单为空数组！！！");
				} else {
					if(list.get(0).getOrderType().shortValue() == OrderTypeEnum.LUNCH.getCode().shortValue()) {
						log.warn("该订单为午餐订单，忽略");
						return;
					}
					
					list.stream().forEach(item -> {
						if(CollectionUtils.isEmpty(item.getOrderDetails())) {
							log.error("订单：{}未包含明细数据", item.getOrderNo());
							throw new RuntimeException("订单数据没有商品数据");
						}
					});
					this.orderService.save(list);
				}
			} catch (Exception e) {
				log.error("订单数据转换成list异常", e);
				throw e;
			}
		} else {
			log.warn("收到订单的空消息！！！");
		}
	}
	
	/**
	 * 订单退款消费
	 * @param message
	 */
	@StreamListener(MqMessageOrderSource.ORDER_REFUND_INPUT)
	public void orderRefund(Message<String> message) {
		log.info("开始消费订单退款信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			List<OrderRefundDto> list = JSONObject.parseArray(payload, OrderRefundDto.class);
			if(CollectionUtils.isEmpty(list)) {
				log.warn("退款信息为空数组！！！");
			} else {
				if(null==list.get(0).getOrderType() || OrderTypeEnum.LUNCH.getCode().shortValue()==list.get(0).getOrderType().shortValue()) {
					log.warn("该订单退款为午餐的，忽略！");
					return;
				}
				
				this.orderService.refund(list);
			}
		} else {
			log.warn("收到订单退款的空消息！！！");
		}
	}
	
	/**
	 * 转换配送单到自己内部的订单
	 * @param list
	 * @return
	 */
	private List<OrderDto> convertOrderDto(List<OrderDeliveryMessage> list) {
		if(null != list && !list.isEmpty()) {
			List<OrderDto> result = new ArrayList<>(list.size());
			list.stream().forEach(item -> {
				OrderDto orderDto = new OrderDto();
				orderDto.setId(item.getId());
				orderDto.setActualPayAmount(item.getShareOrderPayAmount());
				orderDto.setDeliveryAddress(null);
				orderDto.setDeliveryCompany(item.getDeliveryCompany());
				orderDto.setDeliveryCostAmount(item.getDeliveryActualAmount());
				orderDto.setDeliveryEndDate(item.getDeliveryTimezoneTo());
				orderDto.setDeliveryFloor(item.getDeliveryFloor());
				// 楼宇id的字段里存了公司id
				orderDto.setDeliveryCompanyId(item.getDeliveryBuildingId());
				orderDto.setDeliveryRoom(null);
				orderDto.setDeliveryStartDate(item.getDeliveryTimezoneFrom());
				// 
				orderDto.setDiscountAmount(item.getGoodsActualAmount());
				orderDto.setMobileNo(item.getMobileNo());
				orderDto.setOrderNo(item.getDeliveryNo());
				orderDto.setOrderStatus(OrderStatusEnum.WAITPACK.getValue().shortValue());
				if(null == item.getOrderType()) {
					orderDto.setOrderType(new Short(OrderTypeEnum.BREAKFAST.getCode()));
				} else {
					orderDto.setOrderType(new Short(item.getOrderType()));
				}
				orderDto.setPackageAmount(item.getPackageActualAmount());
				orderDto.setRealName(item.getRealName());
				orderDto.setRedPacketAmount(item.getRedPacketAmount());
				orderDto.setShopCode(item.getShopCode());
				orderDto.setTotalAmount(item.getTotalActualAmount());
				orderDto.setUserId(item.getUserId());
				// 取餐点
				orderDto.setDeliveryRoom(item.getTakeMealsAddrName());
				
				List<OrderDeliveryGoodsMessage> details = item.getDeliveryGoods();
				if(null!=details && !details.isEmpty()) {
					List<OrderDetailDto> orderDetails = new ArrayList<>(details.size());
					
					details.stream().forEach(detail -> {
						orderDetails.add(convertOrderDetail(detail, true));
					});
					
					orderDto.setOrderDetails(orderDetails);
				}
				
				result.add(orderDto);
				
			});
			return result;
		}
		
		return null;
	}
	
	/**
	 * 转换订单明细
	 * @param detail
	 * @return
	 */
	private OrderDetailDto convertOrderDetail(OrderDeliveryGoodsMessage detail, boolean isMainDetail) {
		OrderDetailDto d = new OrderDetailDto();
		d.setCreateTime(null);
		d.setUpdateTime(null);
		
		d.setGoodsCode(detail.getGoodsCode());
		d.setGoodsName(detail.getGoodsName());
		d.setGoodsCount(detail.getGoodsCount());
		
		d.setId(detail.getId());
		d.setOrderId(detail.getDeliveryId());
		d.setOrderNo(detail.getDeliveryNo());
		
		if(isMainDetail) {
			d.setGoodsDiscountPrice(detail.getGoodsActualPrice());
			d.setGoodsPrice(detail.getGoodsOriginalPrice());
		}
		
		// 是组合商品
		if(Boolean.TRUE.equals(detail.getIsCombo())) {
			if(CollectionUtils.isEmpty(detail.getComboDetails())) {
				throw new RuntimeException("商品：" + detail.getGoodsName() + "为组合商品，但没有组合的明细信息") ;
			}
			d.setIsCombo(Boolean.TRUE);
			
			List<OrderDetailDto> comboDetails = new ArrayList<>(detail.getComboDetails().size());
			detail.getComboDetails().stream().forEach(combo -> {
				comboDetails.add(convertOrderDetail(combo, false));
			});
			d.setComboDetails(comboDetails);
		} else {
			d.setIsCombo(Boolean.FALSE);
		}
		return d;
	}
}
