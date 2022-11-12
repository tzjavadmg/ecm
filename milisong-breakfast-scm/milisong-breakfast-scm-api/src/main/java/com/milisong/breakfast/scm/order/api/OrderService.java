package com.milisong.breakfast.scm.order.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.order.dto.mq.OrderRefundDto;
import com.milisong.breakfast.scm.order.dto.param.OrderDetailComboParam;
import com.milisong.breakfast.scm.order.dto.param.OrderGoodsSumParam;
import com.milisong.breakfast.scm.order.dto.param.OrderReserveSearchParamDto;
import com.milisong.breakfast.scm.order.dto.param.OrderSearch4OrderSetParam;
import com.milisong.breakfast.scm.order.dto.param.OrderSearchParam;
import com.milisong.breakfast.scm.order.dto.param.OrderUpdateStatusParam;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailComboDto;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailDto;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailResult;
import com.milisong.breakfast.scm.order.dto.result.OrderDto;
import com.milisong.breakfast.scm.order.dto.result.OrderExportResult;
import com.milisong.breakfast.scm.order.dto.result.OrderReserveSearchResult;
import com.milisong.breakfast.scm.order.dto.result.OrderSearchResult;

public interface OrderService {
	/**
	 * 集单用查询订单主表信息
	 * @return
	 */
	List<OrderDto> search4OrderSet(OrderSearch4OrderSetParam param);
	
	/**
	 * 根据订单号和组合商品code查询组合商品详情
	 * @param param
	 * @return
	 */
	List<OrderDetailComboDto> searchOrderDetailComboList(OrderDetailComboParam param);
	
	/**
	 * 根据订单号获取订单详情
	 * @param orderNo
	 * @return
	 */
	List<OrderDetailDto> getOrderDetailByOrderNo(String orderNo);
		
	/**
	 * 保存从MQ去接收来的订单信息
	 * @param orderDto
	 */
	void save(List<com.milisong.breakfast.scm.order.dto.mq.OrderDto> orderDtoList);
	
	/**
	 * 统计商品已售总数量
	 * @param param
	 * @return
	 */
	int getOrderGoodsCount(OrderGoodsSumParam param);
		
	/**

	 * 更新订单状态
	 * @param param
	 */
	void updateStatus(OrderUpdateStatusParam param);

	/**
	 * 根据订单号查询订单明细信息
	 * @param orderNo
	 * @return
	 */
	List<OrderDetailResult> getOrderDetailInfoByOrderNo(List<String> orderNos);

	/**
	 * 订单退款处理
	 * @param list
	 */
	void refund(List<OrderRefundDto> list);
	
	/**
	 * 门店商品预定量查询
	 * @param param
	 * @return
	 */
	Pagination<OrderReserveSearchResult> pageSearchReserveOrder(OrderReserveSearchParamDto param);
	
	/**
	 * 更新订单的集单处理状态
	 * @param orderNoList
	 */
	void updateOrderProcessStatus(List<String> orderNoList);

	
	/**
	 * 查询公司的订单里的取餐点
	 * @param compayId
	 * @return
	 */
	List<String> searchCompanyMealAddress(Long companyId);
	
	/**
	 * 查询今天配送的所有订单号
	 * @return
	 */
	List<String> getTodayAllOrderNo();
}
