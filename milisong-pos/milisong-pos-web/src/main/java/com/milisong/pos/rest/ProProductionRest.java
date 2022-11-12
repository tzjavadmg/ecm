package com.milisong.pos.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pos.production.api.PosProductionService;
import com.milisong.pos.production.constant.OrderSetStatusEnum;
import com.milisong.pos.production.dto.result.OrderSetInfoResult;
import com.milisong.pos.production.param.PosProductionParam;
import com.milisong.upms.utils.UserInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年10月26日---下午4:46:32
*
*/
@Slf4j
@RestController
@RequestMapping("/proproduction")
public class ProProductionRest {

	@Autowired
	private PosProductionService posproductionService;
	
	@PostMapping("/get-list-by-shopid-status")
	public ResponseResult<Pagination<OrderSetInfoResult>> getListByShopIdAndStatus(@RequestBody PosProductionParam posProductionParam) {
		Long shopId = posProductionParam.getShopId();
		if(null == shopId) {
			return ResponseResult.buildFailResponse("9999","无门店权限 ");
		}
		return ResponseResult.buildSuccessResponse(posproductionService.getOrderSetListByStatus(posProductionParam));
	}
	
	/**
	 * 生产完成
	 * @param setNo
	 * @return
	 */
	@GetMapping("/finish-order")
	public ResponseResult<String> getListByShopIdAndStatus(@RequestParam("setNo")String setNo,@RequestParam(name ="shopId",required=false)Long shopId) {
		if(null == shopId) {
			return ResponseResult.buildFailResponse("9999","无门店权限 ");
		}
		log.info("生产完成单号:{},shopId{}",setNo,shopId);
		return	posproductionService.updateOrderSetStatusByNo(shopId,setNo,OrderSetStatusEnum.FINISH_ORDER_3.getValue(),UserInfoUtil.buildUpdateBy());
	}
	
	/**
	 * 暂停生成
	 * @param shopId
	 * @param type
	 * @return
	 */
	@GetMapping("/suspend-production")
	public ResponseResult<String> suspendProduction(@RequestParam(name ="shopId",required=false) Long shopId,@RequestParam("type")String type){
		log.info("暂停门店生产{},类型{}",shopId,type);
		if(null == shopId) {
			return ResponseResult.buildFailResponse("9999","无门店权限 ");
		}
		posproductionService.pauseOrRestart(shopId, UserInfoUtil.buildUpdateBy());
		return ResponseResult.buildSuccessResponse();
	}
	
	@GetMapping("/get-shop-production-status")
	public ResponseResult<Integer> getShopProductionStatus(@RequestParam(name ="shopId",required=false) Long shopId){
		if(null == shopId) {
			return ResponseResult.buildFailResponse("9999","无门店权限 ");
		}
		return ResponseResult.buildSuccessResponse(posproductionService.getShopProductionStatus(shopId));
	}
	
}
