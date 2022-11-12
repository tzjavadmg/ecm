package com.milisong.oms.task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.constant.OrderType;
import com.milisong.oms.domain.Order;
import com.milisong.oms.mapper.OrderDeliveryMapper;
import com.milisong.oms.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class OrderTask {

	@Autowired
	OrderDeliveryMapper orderDeliveryMapper;
	
	@Autowired
	OrderMapper orderMapper;
	
    @Resource
    private SystemProperties systemProperties;
	
    @RequestMapping(value = "/v1/order/update-order-status", method = RequestMethod.GET)
	public void orderStatusTask() {
		log.info("-----------订单状态修复开始-----------");
		updateOrderDeliveryStatus();
		checkOrderStatus();
		log.info("-----------订单状态修复结束-----------");
	}
	/**
	 * 下线配送
	 */
	private void updateOrderDeliveryStatus() {
		// 修改今天之前的配送订单状态 将备餐中的改为已完成
		log.info("修改今天之前的配送订单状态");
		log.info("加载配置->超过期望送达时间{}小时",systemProperties.getNodeHour());
		int row = orderDeliveryMapper.updateOrderDeliveryStatus(systemProperties.getNodeHour());
		log.info("受影响的配送订单数量:{}",row);
	}
	
	private void checkOrderStatus() {
		log.info("根据配送单状态来修改主订单状态");
		// 查询订单状态为已支付 并且是 早餐
		List<Order> listOrder = orderMapper.selectOrderByStatusAndOrderType(OrderStatus.PAID.getValue());
		if(CollectionUtils.isEmpty(listOrder)) {
			log.info("没有需要处理的订单");
			return;
		}
		//查询配送单状态为备餐中   如果不存在 则修改主订单状态为已完成
		List<Long> orderId = listOrder.stream().map(Order::getId).collect(Collectors.toList());
		log.info("主订单数量:{}",orderId.size());
		List<Long> newOrderId = orderDeliveryMapper.checkOrderDeliveryStatusCount(orderId);
		if(CollectionUtils.isEmpty(newOrderId)) {
			log.info("需要处理的订单id:{}",JSON.toJSONString(newOrderId));
			orderMapper.updateOrderStatusByOrderId(orderId);
		}else {
			List<Long> orderIds = removeRepeat(newOrderId, orderId);
			log.info("需要处理的订单id:{}",JSON.toJSONString(orderIds));
			if(!CollectionUtils.isEmpty(orderIds)) {
				orderMapper.updateOrderStatusByOrderId(orderIds);
			}
		}
	}
	
	private List<Long> removeRepeat(List<Long> newList,List<Long> oldList){
		List<Long> resultList = new ArrayList<Long>();
		if(CollectionUtils.isEmpty(newList)) {
			return null;
		}
		oldList.forEach(item->{
			if(!newList.contains(item)) {
				resultList.add(item);
			}
		});
		return resultList;
	}
}
