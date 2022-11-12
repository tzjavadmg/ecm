package com.milisong.pos.production.mapper;

import java.util.List;

import com.milisong.pos.production.domain.OrderSetDetailGoods;

/**
*@author    created by benny
*@date  2018年10月25日---下午8:28:19
*
*/
public interface OrderSetDetailGoodsMapper {
	/**
	 * 批量保存
	 * @param record
	 * @return
	 */
	int insertBatchSelective(List<OrderSetDetailGoods> record);
}
