package com.milisong.breakfast.scm.goods.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.goods.dto.ShopGoodsSalableQuantityDto;
import com.milisong.breakfast.scm.goods.param.GoosSalableParam;
import com.milisong.breakfast.scm.goods.param.ShopGoodsSalableQuantityParam;
import com.milisong.scm.base.dto.ShopDto;

/**
*@author    created by benny
*@date  2018年12月4日---下午11:04:38
*
*/
public interface ShopGoodsSalableQuantityService {

	public void initShopGoodsSalableQuantity(Map<Date,List<String>> goodsMap, ShopDto shopDto);
	
	/**
	 * 修改可售量
	 * @return
	 */
	public ResponseResult<String> update(ShopGoodsSalableQuantityParam shopGoodsSalableQuantityParam,String orderGoodsCountUrl);
	
	/**
	 * 根据门店ID和日期查询
	 * @param week 
	 * @param yrear 
	 * @param weekOfYear 
	 * @param shopId 
	 * @return
	 */
	public ResponseResult<Pagination<ShopGoodsSalableQuantityDto>> getByShopIdAndDate(GoosSalableParam goodsSalableParam,String orderGoodsCountUrl);
}
