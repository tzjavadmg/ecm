package com.milisong.scm.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.milisong.scm.base.api.DimDateService;
import com.milisong.scm.constant.SysConstant;
import com.milisong.scm.properties.OssUrlProperties;
import com.milisong.upms.constant.SsoErrorConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.stock.api.ShopOnsaleGoodsStockService;
import com.milisong.scm.stock.dto.PreProductionDto;
import com.milisong.scm.stock.dto.ShopOnsaleGoodsStockDto;
import com.milisong.scm.stock.param.ShopOnsaleGoodsStockParam;
import com.milisong.upms.utils.UserInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年9月5日---下午2:01:50
*
*/
@Slf4j
@RestController
@RequestMapping("/stock")
public class StockRest {
	
	@Autowired(required=false)
	private ShopOnsaleGoodsStockService onsaleGoodsStockService;
	
	@Autowired(required=false)
	private DimDateService dimDateService;

	@Autowired
	private OssUrlProperties ossUrlProperties;
	
	@GetMapping("/get-work-date-now")
	public ResponseResult<List<String>> getWorkDateNow(@RequestParam("size") Integer size){
		log.info("获取最近{}天的工作日期",size);
		return dimDateService.getWorkDateByNow(size);
	}

	@GetMapping("/get-goods-pre-production")
	public ResponseResult<List<PreProductionDto>> goodsPreProduction() {
		ResponseResult<List<Date>> listDay = dimDateService.getWorkDateByBeforeNow(5);
	return 	onsaleGoodsStockService.goodsPreProductionByWorkDay(listDay.getData());
	}
	
	@GetMapping("/get-day-goods-pre-production")
	public ResponseResult<Map<String,Object>> goodsDayPreProduction(String shopId) {
		ResponseResult<List<Date>> listDay = dimDateService.getWorkDateByBeforeNow(5);
	return 	onsaleGoodsStockService.goodsDayPreProductionByWorkDay(listDay.getData(),shopId);
	}
	/**
	 * 分页条件查询门店可售
	 * @return
	 */
	@PostMapping("/page-search-by-condition")
	public ResponseResult<Pagination<ShopOnsaleGoodsStockDto>> pageSearchByCondition(@RequestBody ShopOnsaleGoodsStockParam onsaleGOodsStockParam) {
		ResponseResult<String> checkResult = checkShopPermission(onsaleGOodsStockParam.getShopId());
		if(!checkResult.isSuccess()){
			return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
		}
		log.info("分页查询：{}",JSONObject.toJSONString(onsaleGOodsStockParam));
		try {
			if(null != onsaleGOodsStockParam) {
				if(StringUtils.isBlank(onsaleGOodsStockParam.getSaleDate())) {
					return ResponseResult.buildFailResponse("9999","参数错误");
				}
			}
			Pagination<ShopOnsaleGoodsStockDto> pagination = onsaleGoodsStockService.pageSearchByCondition(onsaleGOodsStockParam,ossUrlProperties.getGoodsCount());
			return ResponseResult.buildSuccessResponse(pagination);
		} catch (Exception e) {
			log.error("调用门店可售分页查询出现异常", e);
			throw e;
		}
	}
	
	/**
	 * 设置商品可售数量
	 * @param onsaleGOodsStockParam
	 * @return
	 */
	@PostMapping("/update-by-condition")
	public ResponseResult<Object> updateByCondition(@RequestBody ShopOnsaleGoodsStockParam onsaleGOodsStockParam) {
		try {
			onsaleGOodsStockParam.setOperator(UserInfoUtil.buildUpdateBy());
			return onsaleGoodsStockService.updateByCondition(onsaleGOodsStockParam,ossUrlProperties.getGoodsCount());
		} catch (Exception e) {
			log.error("调用门店商品可售数量设置出现异常", e);
			throw e;
		}
	}
	
	/**
	 * 初始化商品可售数量
	 * @param onsaleGOodsStockParam
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/init-data-stock")
	public ResponseResult<Object> initDataByStock(@RequestParam("saleDate") String saleDate) throws Exception {
		SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			onsaleGoodsStockService.testInitOnsaleGoodsStock(smp.parse(saleDate));
			} catch (Exception e) {
			log.error("调用门店商品可售数量设置出现异常", e);
			throw e;
		}
		return ResponseResult.buildSuccessResponse("操作成功");
	}
	
	@GetMapping("/testInitDataByStock")
	public ResponseResult<Object> testInitDataByStock(){
		onsaleGoodsStockService.initOnsaleGoodsStockV2();
		return ResponseResult.buildSuccessResponse();
	}

	private ResponseResult<String> checkShopPermission(Long shopId){
		if(!UserInfoUtil.checkShopPermission(shopId)){
			return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(),SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
		}else{
			return ResponseResult.buildSuccessResponse();
		}
	}
	
}
