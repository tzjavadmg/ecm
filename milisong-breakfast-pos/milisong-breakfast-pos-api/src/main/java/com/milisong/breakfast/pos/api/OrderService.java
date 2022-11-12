package com.milisong.breakfast.pos.api;

import com.alibaba.fastjson.JSONArray;

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

    /**
     * 订单状态变更MQ处理
     * @param msg
     */
	void orderStatusChange(String msg);
}
