package com.milisong.breakfast.scm.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.goods.api.GoodsScheduleService;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleInfoDto;
import com.milisong.breakfast.scm.goods.param.GoodsScheduleParam;
import com.milisong.upms.utils.UserInfoUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年12月3日---下午8:42:06
*
*/
@Api(tags = "档期管理")
@Slf4j
@RestController
@RequestMapping("/goods-schedule")
public class GoodsScheduleRest {

	@Autowired
	private GoodsScheduleService goodsScheduleService;
	
	/**
	 * 获取档期日期
	 * @param year
	 * @param weekOfYear
	 * @param shopId
	 * @return
	 */
	@ApiOperation("获取档期日期")
	@GetMapping("/get-goods-schedule-by-week")
	public ResponseResult<GoodsScheduleInfoDto> getGoodsScheduleInfoByWeek(@RequestParam("year")Integer year,@RequestParam("weekOfYear")String weekOfYear,@RequestParam("shopId")Long shopId){
		log.info("获取档期日期year:{},weekOfYear:{},shopId{}",year,weekOfYear,shopId);
		String updateUser = UserInfoUtil.buildUpdateBy();
		return goodsScheduleService.getGoodsScheduleByWeekOfYear(year, weekOfYear, shopId,updateUser);
	}
	
	/**
	 * 保存档期
	 * @param goodsScheduleParam
	 * @return
	 */
	@ApiOperation("保存档期")
	@PostMapping("/save-goods-schedule-Info")
	public ResponseResult<String> saveGoodsScheduleInfo(@RequestBody GoodsScheduleParam goodsScheduleParam){
		log.info("保存档期{}",JSON.toJSONString(goodsScheduleParam));
		String updateUser = UserInfoUtil.buildUpdateBy();
		goodsScheduleParam.setUpdateBy(updateUser);
		return goodsScheduleService.saveGoodsSchedule(goodsScheduleParam);
	}
	/**
	 * 获取可售档期日期
	 * @param shopId
	 * @return
	 */
	@ApiOperation("获取可售档期日期")
	@GetMapping("/get-date-by-shopId")
	public ResponseResult<List<GoodsScheduleDto>> getDateByShopId(@RequestParam("shopId") Long shopId){
		return goodsScheduleService.getDateByShopId(shopId);
	}
	
	@ApiOperation("档期发布")
	@GetMapping("/goods-schedule-task")
	public void goodsScheduleTask() {
		log.info("档期商品上线发布");
		goodsScheduleService.publishGoodsSchedule();
	}
}
