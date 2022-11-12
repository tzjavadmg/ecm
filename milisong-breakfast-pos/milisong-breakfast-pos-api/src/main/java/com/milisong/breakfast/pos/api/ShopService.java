package com.milisong.breakfast.pos.api;

import java.util.List;

import com.milisong.breakfast.pos.dto.ShopDto;

/**
*@author    created by benny
*@date  2018年11月12日---下午6:55:09
*
*/
public interface ShopService {

	/**
	 * 查询门店信息
	 * @return
	 */
	public List<ShopDto> getShopList();
	
	/**
	 * 消费门店MQ
	 * @param message
	 */
	public void shopInfoByMq(String message);
}
