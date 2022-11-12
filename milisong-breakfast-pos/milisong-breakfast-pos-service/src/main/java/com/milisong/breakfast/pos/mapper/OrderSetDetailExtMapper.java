package com.milisong.breakfast.pos.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.breakfast.pos.dto.result.OrderSetInfoResult;

public interface OrderSetDetailExtMapper {
	
	/**
	 * 分页条件查询不同状态的门店订单信息
	 * @param posproductionParam
	 * @return
	 */
	List<OrderSetInfoResult> getListByShopIdAndStatus(Map<String,Object> map);
	
	int getCountByShopIdAndStatus(Map<String,Object> map);	
}