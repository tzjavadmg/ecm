package com.milisong.pos.production.api;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.milisong.pos.production.dto.OrderDto;
import com.milisong.pos.production.dto.OrderRefundDto;

/**
*@author    created by benny
*@date  2018年10月30日---上午11:31:21
*
*/
public interface OrderService {

	/**
	 * 订单MQ处理、
	 * @param list
	 */
	void saveOrder(JSONArray list);
	
	/**
	 * 退单MQ处理
	 * @param list
	 */
	void refoundOrder(JSONArray list);

	void saveOrder(List<OrderDto> list);

	void refoundOrder(List<OrderRefundDto> list);
}
