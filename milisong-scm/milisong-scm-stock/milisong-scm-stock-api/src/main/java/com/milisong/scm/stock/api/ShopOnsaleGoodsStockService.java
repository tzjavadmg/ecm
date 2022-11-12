package com.milisong.scm.stock.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.stock.dto.PreProductionDto;
import com.milisong.scm.stock.dto.ShopOnsaleGoodsStockDto;
import com.milisong.scm.stock.param.ShopOnsaleGoodsStockParam;

/**
 * @author benny
 *
 */
public interface ShopOnsaleGoodsStockService {
	
	/**
	 * 分页条件查询门店可售
	 * @return
	 */
	public Pagination<ShopOnsaleGoodsStockDto> pageSearchByCondition(ShopOnsaleGoodsStockParam onsaleGOodsStockParam,String goodsCountUrl);
	
	/**
	 * 设置商品可售数量
	 * @param onsaleGOodsStockParam
	 * @return
	 */
	public  ResponseResult<Object> updateByCondition(ShopOnsaleGoodsStockParam onsaleGOodsStockParam,String goodsCountUrl);
	
	/**
	 * 初始化商品可售数量
	 * @return
	 */
	public void initOnsaleGoodsStock();
	
	
	/**
	 * 测试接口、生成可售商品库存
	 * 
	 */
	public void testInitOnsaleGoodsStock(Date sale_date);
	
	void initOnsaleGoodsStockV2();

	public ResponseResult<List<PreProductionDto>> goodsPreProductionByWorkDay(List<Date> listDay);

	public ResponseResult<Map<String, Object>> goodsDayPreProductionByWorkDay(List<Date> listDay, String shopId);
}
