package com.milisong.scm.goods.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.goods.dto.GoodsSalableQuantityLunchDto;
import com.milisong.scm.goods.param.GoodsSalableQuantityLunchParam;
import com.milisong.scm.goods.param.GoosSalableLunchParam;

/**
*@author    created by benny
*@date  2018年12月4日---下午11:04:38
*
*/
public interface GoodsSalableQuantityLunchService {

	public void initShopGoodsSalableQuantity(Map<Date,List<String>> goodsMap, ShopDto shopDto);
	
	/**
	 * 修改可售量
	 * @return
	 */
	public ResponseResult<String> update(GoodsSalableQuantityLunchParam shopGoodsSalableQuantityParam);
	
	/**
	 * 根据门店ID和日期查询
	 * @param week 
	 * @param yrear 
	 * @param weekOfYear 
	 * @param shopId 
	 * @return
	 */
	public ResponseResult<Pagination<GoodsSalableQuantityLunchDto>> getByShopIdAndDate(GoosSalableLunchParam goodsSalableParam);

}
