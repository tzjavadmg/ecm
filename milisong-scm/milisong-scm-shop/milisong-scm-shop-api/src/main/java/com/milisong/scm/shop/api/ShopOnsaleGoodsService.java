package com.milisong.scm.shop.api;

import java.util.List;

import com.milisong.scm.shop.dto.goods.GoodsInfoDto;
import com.milisong.scm.shop.dto.shop.ShopOnsaleGoodsDto;

/**
 * @author benny
 *
 */
public interface ShopOnsaleGoodsService {
	
	/**
	 * 获取可售商品
	 * @return
	 */
	List<ShopOnsaleGoodsDto> getByCondition();
	
	/**
	 * 新增可售商品
	 * @param shopOnsaleGoodsDto
	 * @return
	 */
	int insert(ShopOnsaleGoodsDto shopOnsaleGoodsDto, List<GoodsInfoDto> goodsInfos);
	
	/**
	 * 更新可售商品信息
	 * @param shopOnsaleGoodsDtoList
	 * @return
	 */
	int update(List<ShopOnsaleGoodsDto> shopOnsaleGoodsDtoList, List<GoodsInfoDto> goodsInfos);
	
}
