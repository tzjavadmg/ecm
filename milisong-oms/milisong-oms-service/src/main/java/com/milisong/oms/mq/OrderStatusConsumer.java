package com.milisong.oms.mq;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.constant.DeliveryStatus;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.dto.OrderDeliveryMqDto;
import com.milisong.oms.dto.OrderMqDto;
import com.milisong.oms.mapper.OrderDeliveryMapper;
import com.milisong.oms.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class OrderStatusConsumer {
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private OrderDeliveryMapper orderDeliveryMapper;
	
	@Resource
	private OrderMapper orderMapper;
	
	@StreamListener(MessageSink.MESSAGE_ORDER_INTPUT)
	 public void accept(Message<String> message) {
        log.info("接收订单的消息，参数={}", JSONObject.toJSONString(message));
        try{
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
			OrderMqDto msgDto = JSONObject.parseObject(msg, OrderMqDto.class);
			OrderDeliveryMqDto deliveryMqDto = new OrderDeliveryMqDto();
			if (msgDto.getStatus() == (byte)2&&msgDto.getSfStatus() == (byte)0) {
				deliveryMqDto.setStatus((byte) DeliveryStatus.PACKAGED.getValue());
				deliveryMqDto.setPackageTime(msgDto.getDate());//打包时间
			}
			if (msgDto.getStatus() == (byte)3) {
				deliveryMqDto.setStatus((byte) DeliveryStatus.DELIVERING.getValue());  
				deliveryMqDto.setDeliveryStartTime(msgDto.getDate());//配送开始时间
			}
			if (msgDto.getStatus() == (byte)4) {
				deliveryMqDto.setStatus((byte) DeliveryStatus.COMPLETED.getValue()); 
				deliveryMqDto.setDeliveryEndTime(msgDto.getDate());//配送完成时间
			}
			deliveryMqDto.setDeliveryNo(msgDto.getOrderNo());
			deliveryMqDto.setSfStatus(msgDto.getSfStatus());
			//判断如果是送达补偿过来的消息，就将订单的配送开始结束时间设定为预期时间
			dealCompensateCompletedMsg(msgDto,deliveryMqDto);
			//根据配送单号更新时间及状态
			orderService.updateDeliveryByOrderNo(deliveryMqDto);
 			
			List<Byte> statusList = Lists.newArrayList();
			statusList.add(DeliveryStatus.REFUNDED.getValue());
			statusList.add(DeliveryStatus.COMPLETED.getValue());
			List<OrderDelivery> orderDeliveryList = orderDeliveryMapper.findFinishOrder(msgDto.getOrderNo(), statusList);//查询已完成的配送单
			log.info("查询已完成配送单信息{}",JSON.toJSONString(orderDeliveryList));
			if (CollectionUtils.isNotEmpty(orderDeliveryList)) {
				Long orderId = orderDeliveryList.get(0).getOrderId();
				Order order = orderMapper.selectByPrimaryKey(orderId);
				if (order != null) {
					int totalOrderDays = order.getTotalOrderDays(); //订单天数
					int finishDays = orderDeliveryList.size(); //已完成天数配送单
					log.info("订单天数={},已完成天数={}",totalOrderDays,finishDays);
					if (totalOrderDays == finishDays) { //配送单全部完成更新主订单状态为已完成
						Order param = new Order();
						param.setId(orderId);
						param.setStatus(OrderStatus.COMPLETED.getValue());
						orderMapper.updateByPrimaryKeySelective(param); 
					}
				}
			}
			
        }catch(Exception e) {
			e.printStackTrace();
			log.error("接收消息结束，出现异常", e);
        }

	 }

	/**
	 * 此方法用于处理有些订单走线下配送，数据库里的配送开始结束时间为空，现在处理为预期时间
	 * @param deliveryMqDto 处理的dto，后面此dto会更新到数据库
	 * @param msgDto 接收到的mq消息
	 */
	private void dealCompensateCompletedMsg(OrderMqDto msgDto,OrderDeliveryMqDto deliveryMqDto) {
		Boolean isCompensate = msgDto.getIsCompensate();
		if(isCompensate == null || !isCompensate){
			return;
		}
		//只处理状态4的消息，这一步只是为了确保不误处理
		if (msgDto.getStatus() != (byte)4) {
			return;
		}
		//开始处理查出预期送达时间，然后set到dto里
		OrderDelivery orderDelivery = orderDeliveryMapper.getDeliveryTimezoneByNo(msgDto.getOrderNo());
		if(orderDelivery!=null){
			deliveryMqDto.setDeliveryStartTime(orderDelivery.getDeliveryTimezoneFrom());
			deliveryMqDto.setDeliveryEndTime(orderDelivery.getDeliveryTimezoneTo());
		}
	}

}
