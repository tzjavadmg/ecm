package com.milisong.scm.orderset.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.orderset.dto.mq.OrderRefundDto;
import com.milisong.scm.orderset.dto.param.*;
import com.milisong.scm.orderset.dto.result.*;

public interface OrderService {
	
	/**
	 * 查询有订单的公司-订单汇总信息
	 * @param param
	 * @return
	 */
	Pagination<OrderCompanyResult> pageSearchExistsOrderCompany(OrderSearch4OrderSetParam param);
	
	/**
	 * 根据公司信息查询详细的最新的配送地址
	 * @param param
	 * @return
	 */
	String queryDeliveryAddressByCompanyInfo(OrderSearch4OrderSetParam param);
		
	/**
	 * 集单用查询订单详情信息
	 * 楼宇id、公司、日期为必传
	 * @return
	 */
	List<OrderDetailResult> search4OrderSet(OrderSearch4OrderSetParam param);
		
	/**
	 * 保存从MQ去接收来的订单信息
	 * @param orderDto
	 */
	void save(List<com.milisong.scm.orderset.dto.mq.OrderDto> orderDtoList);
	
	/**
	 * 统计商品已售总数量
	 * @param param
	 * @return
	 */
	int getOrderGoodsCount(OrderGoodsSumParam param);
	
	/**
	 * 根据订单号查询主订单信息
	 * @param orderNo
	 * @return
	 */
	OrderResult getOrderInfoByOrderNo(String orderNo);
	
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
	Pagination<OrderReserveSearchResultDto> pageSearchReserveOrder(OrderReserveSearchParamDto param);
	
	/**
	 * 更新订单的集单处理状态
	 * @param orderNoList
	 */
	void updateOrderProcessStatus(List<String> orderNoList);

	/**
	 * 分页查询订单信息
	 * @param param
	 * @return
	 */
	ResponseResult<Pagination<OrderSearchResult>> pageSearchOrder(OrderSearchParam param);

	/**
	 * 列表查询订单信息
	 * @param param
	 * @return
	 */
	List<OrderExportResult> exportOrder(OrderSearchParam param);
	
	/**
	 * 查询今天配送的所有订单号
	 * @return
	 */
	List<String> getTodayAllOrderNo();
}
