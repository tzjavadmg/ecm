package com.milisong.breakfast.scm.goods.api;

import java.util.List;

import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDetailDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleInfoDto;
import com.milisong.breakfast.scm.goods.param.GoodsScheduleParam;

/**
*@author    created by benny
*@date  2018年12月3日---下午6:50:37
*
*/
public interface GoodsScheduleService {

	/**
	 * 根据周查询档期
	 * @param year
	 * @param weekOfYear
	 * @param shop_id
	 * @return
	 */
	public ResponseResult<GoodsScheduleInfoDto> getGoodsScheduleByWeekOfYear(Integer year,String weekOfYear,Long shopId,String updateBy);
	
	/**
	 * 获取档期中的商品信息
	 * @param year
	 * @param weekOfYear
	 * @param shopId
	 * @return
	 */
	public List<GoodsScheduleDetailDto> getGoodsScheduleDetailByWeekOfYear(Integer year,Integer weekOfYear,Long shopId);

	/**
	 * 保存商品排期
	 * @param goodsScheduleParam
	 * @return
	 */
	public ResponseResult<String> saveGoodsSchedule(GoodsScheduleParam goodsScheduleParam);
	 
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
	public ResponseResult<List<GoodsScheduleDto>> getDateByShopId(Long shopId);
}
