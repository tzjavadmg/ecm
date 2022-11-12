package com.milisong.pos.production.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.pos.production.dto.result.OrderSetInfoResult;
import com.milisong.pos.production.param.PosProductionParam;

/**
*@author    created by benny
*@date  2018年10月25日---下午8:27:58
*
*/
public interface OrderSetDetailMapper {

	
	/**
	 * 分页条件查询不同状态的门店订单信息
	 * @param posproductionParam
	 * @return
	 */
	List<OrderSetInfoResult> getListByShopIdAndStatus(Map<String,Object> map);
	
	int getCountByShopIdAndStatus(Map<String,Object> map);
}
