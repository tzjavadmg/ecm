package com.milisong.breakfast.scm.shop.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.breakfast.scm.shop.domain.ShopSaleFinishConfig;


public interface ShopSaleFinishConfigExtMapper {
    
	/**
	 * 查询门店配置信息[shopId,type (不传查询门店所有配置)]
	 * @param map
	 * @return
	 * youxia 2018年9月5日
	 */
    List<ShopSaleFinishConfig> getShopSaleFinishByShopId(Map<String, Object> map);
}