package com.mili.oss.mq;

import java.util.ArrayList;
import java.util.List;

import com.milisong.scm.base.dto.mq.CompanyMqDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mili.oss.api.OrderService;
import com.mili.oss.constant.OrderStatusEnum;
import com.mili.oss.constant.OrderTypeEnum;
import com.mili.oss.dto.OrderDeliveryGoodsMessage;
import com.mili.oss.dto.OrderDeliveryMessage;
import com.mili.oss.dto.OrderDto;
import com.mili.oss.dto.OrderGoodsDto;
import com.mili.oss.dto.OrderRefundDto;
import com.mili.oss.dto.orderstatus.DeliveryOrderMqDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MqMessageSource.class)
public class MqMessageConsumer {
	@Autowired
	@Qualifier(value="orderService")
	private OrderService orderService;
//	@Autowired
//	private OrderSetService orderSetService;
	
	/**
	 * 订单消费
	 * @param message
	 */
	@StreamListener(MqMessageSource.ORDER_INPUT)
	public void order(Message<String> message) {
		log.info("开始消费订单信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			try {
				boolean isNew = isNewOrderMsg(payload);
				List<OrderDto> list = null;
				if(isNew) {
					List<OrderDeliveryMessage> nwwList = JSONObject.parseArray(payload, OrderDeliveryMessage.class);
					list = convertOrderDto(nwwList);
					
				} else {
					list = JSONObject.parseArray(payload, OrderDto.class);
				}
				if(CollectionUtils.isEmpty(list)) {
					log.warn("订单为空数组！！！");
				} else {
					/*if(list.get(0).getOrderType().shortValue() == OrderTypeEnum.BREAKFAST.getCode().shortValue()) {
						log.warn("该订单为早餐订单，忽略");
						return;
					}*/
					
					list.stream().forEach(item -> {
						if(CollectionUtils.isEmpty(item.getOrderGoods())) {
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
	@StreamListener(MqMessageSource.ORDER_REFUND_INPUT)
	public void orderRefund(Message<String> message) {
		log.info("开始消费订单退款信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			List<OrderRefundDto> list = JSONObject.parseArray(payload, OrderRefundDto.class);
			if(CollectionUtils.isEmpty(list)) {
				log.warn("退款信息为空数组！！！");
			} else {
//				if(null!=list.get(0).getOrderType() && OrderTypeEnum.BREAKFAST.getCode().shortValue()==list.get(0).getOrderType().shortValue()) {
//					log.warn("该订单退款为早餐的，忽略！");
//					return;
//				}
				this.orderService.refund(list);
			}
		} else {
			log.warn("收到订单退款的空消息！！！");
		}
	}
	
	/**
	 * 顺丰订单状态变更消费
	 * @param message
	 */
	@StreamListener(MqMessageSource.SF_STATUS_CHANGE_INPUT)
	public void sfStatusChange(Message<String> message) {
		log.info("开始消费顺丰订单状态变更信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			DeliveryOrderMqDto dto = JSONObject.parseObject(payload, DeliveryOrderMqDto.class);
 			orderService.updateDistributionStatus(dto);
		} else {
			log.warn("收到顺丰订单状态变更的空消息！！！");
		}
	}
	
	/**
	 * 判断是否是新的订单(配送单)
	 * @param msg
	 * @return
	 */
	private boolean isNewOrderMsg(String msg) {
		JSONArray array = JSONObject.parseArray(msg);
		if(null!=array && !array.isEmpty()) {
			String deliveryNo = array.getJSONObject(0).getString("deliveryNo");
			if(StringUtils.isNotBlank(deliveryNo)) {
				return true;
			}
		}
		return false;
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
			    orderDto.setTakeMealsAddrId(item.getTakeMealsAddrId());
			    orderDto.setTakeMealsAddrName(item.getTakeMealsAddrName());
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
				//orderDto.setDeliveryRoom(item.getTakeMealsAddrName());
				
				List<OrderDeliveryGoodsMessage> details = item.getDeliveryGoods();
				if(null!=details && !details.isEmpty()) {
					List<OrderGoodsDto> orderDetails = new ArrayList<>(details.size());
					
					details.stream().forEach(detail -> {
						orderDetails.add(convertOrderDetail(detail, true));
					});
					
					orderDto.setOrderGoods(orderDetails);
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
	private OrderGoodsDto convertOrderDetail(OrderDeliveryGoodsMessage detail, boolean isMainDetail) {
		OrderGoodsDto d = new OrderGoodsDto();
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
			
			List<OrderGoodsDto> comboDetails = new ArrayList<>(detail.getComboDetails().size());
			detail.getComboDetails().stream().forEach(combo -> {
				comboDetails.add(convertOrderDetail(combo, false));
			});
			d.setComboDetails(comboDetails);
		} else {
			d.setIsCombo(Boolean.FALSE);
		}
		d.setType(detail.getType());
		return d;
	}
	
	/**
	 * 转换配送单到自己内部的订单
	 * @param list
	 * @return
	 */
	private List<OrderDto> convertOrderDto2(List<OrderDeliveryMessage> list) {
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
				orderDto.setDeliveryOfficeBuildingId(item.getDeliveryBuildingId());
				orderDto.setDeliveryRoom(null);
			    orderDto.setTakeMealsAddrId(item.getTakeMealsAddrId());
			    orderDto.setTakeMealsAddrName(item.getTakeMealsAddrName());
				orderDto.setDeliveryStartDate(item.getDeliveryTimezoneFrom());
				// 
				orderDto.setDiscountAmount(item.getGoodsActualAmount());
				orderDto.setMobileNo(item.getMobileNo());
				orderDto.setOrderNo(item.getDeliveryNo());
				orderDto.setOrderStatus(OrderStatusEnum.WAITPACK.getValue().shortValue());
				if(null == item.getOrderType()) {
					orderDto.setOrderType(new Short(OrderTypeEnum.LUNCH.getCode()));
				} else {
					orderDto.setOrderType(new Short(item.getOrderType()));
				}
				orderDto.setPackageAmount(item.getPackageActualAmount());
				orderDto.setRealName(item.getRealName());
				orderDto.setRedPacketAmount(item.getRedPacketAmount());
				orderDto.setShopCode(item.getShopCode());
				orderDto.setTotalAmount(item.getTotalActualAmount());
				orderDto.setUserId(item.getUserId());
				
				List<OrderDeliveryGoodsMessage> details = item.getDeliveryGoods();
				if(null!=details && !details.isEmpty()) {
					List<OrderGoodsDto> orderDetails = new ArrayList<>(details.size());
					
					details.stream().forEach(detail -> {
						OrderGoodsDto d = new OrderGoodsDto();
						d.setCreateTime(null);
						d.setGoodsCode(detail.getGoodsCode());
						d.setGoodsCount(detail.getGoodsCount());
						d.setGoodsDiscountPrice(detail.getGoodsActualPrice());
						d.setGoodsName(detail.getGoodsName());
						d.setGoodsPrice(detail.getGoodsOriginalPrice());
						d.setId(detail.getId());
						d.setOrderId(detail.getDeliveryId());
						d.setOrderNo(detail.getDeliveryNo());
						d.setIsCombo(detail.getIsCombo());
						d.setUpdateTime(null);
						
						orderDetails.add(d);
					});
					
					orderDto.setOrderGoods(orderDetails);
				}
				result.add(orderDto);
				
			});
			return result;
		}
		
		return null;
	}

	/**
	 * 在公司换绑门店时，更新订单数据
	 * @param message
	 */
	@StreamListener(MqMessageSource.OSS_COMPANY_INPUT)
	public void updateOrderAfterSyncCompany(Message<String> message) {
		log.info("开始消费公司信息，参数：{}", JSONObject.toJSONString(message));
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			CompanyMqDto dto = JSONObject.parseObject(payload, CompanyMqDto.class);
			if (dto.getTransformShop()) {
				// 只有公司换绑门店的操作才会更新oss的订单数据
				orderService.updateOrderShop(dto.getId(),dto.getShopId(),dto.getShopCode());
			}
		} else {
			log.warn("接收公司变更信息失败！");
		}
	}

}
