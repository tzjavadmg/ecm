package com.milisong.breakfast.scm.goods.api;

import java.util.List;
import java.util.Set;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.goods.dto.GoodsCombinationRefDto;
import com.milisong.breakfast.scm.goods.dto.GoodsCombinationRefMqDto;
import com.milisong.breakfast.scm.goods.dto.GoodsDto;
import com.milisong.breakfast.scm.goods.param.GoodsParam;
import com.milisong.breakfast.scm.goods.param.GoodsQueryParam;

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
	 * 查询可售商品
	 * @return
	 */
	public ResponseResult<List<GoodsDto>> getGoodsBySchedule(Integer year, Integer weekOfYear,Long shopId);
	
	/**
	 * 根据goodsCode获取商品信息
	 * @param goodsCode
	 * @return
	 */
	public List<GoodsDto> getGoodsInfoByGoodsCodes(List<String> goodsCode);

	public ResponseResult<List<GoodsDto>> getInfoByName(String name);
	
	/**
	 * 根据goodsCode 查询单品信息
	 * @param goodsCode
	 * @return
	 */
	public List<GoodsCombinationRefDto> getGoodsCombinationDtoByGoodsCode(Set<String> goodsCode);
	
	
	public void senGoodsInfoMq(Long id);
}
