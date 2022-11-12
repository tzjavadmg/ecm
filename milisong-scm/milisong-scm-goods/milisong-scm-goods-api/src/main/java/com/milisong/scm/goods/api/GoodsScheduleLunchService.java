package com.milisong.scm.goods.api;

import java.util.List;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.goods.dto.GoodsScheduleDetailLunchDto;
import com.milisong.scm.goods.dto.GoodsScheduleLunchDto;
import com.milisong.scm.goods.dto.GoodsScheduleLunchInfoDto;
import com.milisong.scm.goods.param.GoodsScheduleLunchParam;

/**
*@author    created by benny
*@date  2018年12月3日---下午6:50:37
*
*/
public interface GoodsScheduleLunchService {

	/**
	 * 根据周查询档期
	 * @param year
	 * @param weekOfYear
	 * @param shop_id
	 * @return
	 */
	public ResponseResult<GoodsScheduleLunchInfoDto> getGoodsScheduleByWeekOfYear(Integer year,String weekOfYear,Long shopId,String updateBy);
	
	/**
	 * 获取档期中的商品信息
	 * @param year
	 * @param weekOfYear
	 * @param shopId
	 * @return
	 */
	public List<GoodsScheduleDetailLunchDto> getGoodsScheduleDetailByWeekOfYear(Integer year,Integer weekOfYear,Long shopId);

	/**
	 * 保存商品排期
	 * @param goodsScheduleParam
	 * @return
	 */
	public ResponseResult<String> saveGoodsSchedule(GoodsScheduleLunchParam goodsScheduleParam);
	 
	/**
	 * 档期task
	 * @param param
	 * @return
	 */
	public ResponseResult<String> publishGoodsSchedule();
	
	/**
	 * 根据shopId查询近五天的可售日期
	 * @param shopId
	 * @return
	 */
	public ResponseResult<List<GoodsScheduleLunchDto>> getDateByShopId(Long shopId);
}
