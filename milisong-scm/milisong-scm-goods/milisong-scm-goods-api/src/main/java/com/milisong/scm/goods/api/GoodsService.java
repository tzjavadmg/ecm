package com.milisong.scm.goods.api;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.goods.dto.GoodsDto;
import com.milisong.scm.goods.param.GoodsParam;
import com.milisong.scm.goods.param.GoodsQueryParam;

public interface GoodsService {

	/**
	 * 分页查询可售商品列表
	 * @param param
	 * @return
	 * youxia 2018年9月2日
	 */
	public ResponseResult<Pagination<GoodsDto>> getGoodsPageInfo(GoodsQueryParam param); 
	
	/**
	 * 根据id查询商品明细
	 * @param goodsId 商品id
	 * @return
	 * youxia 2018年9月2日
	 */
	public ResponseResult<GoodsDto> getGoodsDetailsById(Long goodsId);
	
	/**
	 * 保存可售商品信息
	 * @return
	 * youxia 2018年9月2日
	 */
	public ResponseResult<String> saveGoodsInfo(GoodsParam goodsParam);
	
	/**
	 * 定时任务更新商品信息
	 * @return
	 * youxia 2018年9月4日
	 */
	public ResponseResult<String> updateGoodsStatus();
	
}
