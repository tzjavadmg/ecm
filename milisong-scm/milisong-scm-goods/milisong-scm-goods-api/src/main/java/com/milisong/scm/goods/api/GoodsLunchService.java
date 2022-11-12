package com.milisong.scm.goods.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.goods.dto.GoodsLunchDto;
import com.milisong.scm.goods.param.GoodsLunchParam;
import com.milisong.scm.goods.param.GoodsLunchQueryParam;

public interface GoodsLunchService {

	/**
	 * 分页查询可售商品列表
	 * @param param
	 * @return
	 * youxia 2018年9月2日
	 */
	public ResponseResult<Pagination<GoodsLunchDto>> getGoodsLunchPageInfo(GoodsLunchQueryParam param); 
	
	/**
	 * 根据id查询商品明细
	 * @param goodsId 商品id
	 * @return
	 * youxia 2018年9月2日
	 */
	public ResponseResult<GoodsLunchDto> getGoodsLunchDetailsById(Long goodsId);
	
	/**
	 * 保存可售商品信息
	 * @return
	 * youxia 2018年9月2日
	 */
	public ResponseResult<String> saveGoodsLunchInfo(GoodsLunchParam GoodsLunchParam);
	
	/**
	 * 查询可售商品
	 * @return
	 */
	public ResponseResult<List<GoodsLunchDto>> getGoodsLunchBySchedule(Integer year, Integer weekOfYear,Long shopId);
	
	/**
	 * 根据goodsCode获取商品信息
	 * @param goodsCode
	 * @return
	 */
	public List<GoodsLunchDto> getGoodsLunchInfoByGoodsCodes(List<String> GoodsLunchCode);

	public ResponseResult<List<GoodsLunchDto>> getInfoByName(String name);

}
