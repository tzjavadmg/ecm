package com.milisong.pos.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.ResponseResult;
import com.milisong.pos.production.api.PreProductionService;
import com.milisong.pos.production.dto.PreProductionDto;
import com.milisong.upms.utils.UserInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年10月24日---下午7:29:47
*
*/
@Slf4j
@RestController
@RequestMapping("/preproduction")
public class PreProductionRest {

	@Autowired
	private PreProductionService preProductionService;
	
	@GetMapping("/get-list-by-shopid")
	public ResponseResult<List<PreProductionDto>> getListByShopId(@RequestParam(name ="shopId", required = false)Long shopId) {
		log.info("查询门店预生产数量");
		if(null == shopId) {
			return ResponseResult.buildFailResponse("9999","无门店权限 ");
		}
		return preProductionService.getListByShopId(shopId);
	}
	
	@GetMapping("/update-by-id")
	public ResponseResult<String> updateById(@RequestParam("id")Long id,@RequestParam("productionCount")Integer productionCount){
		log.info("修改实际生产数量Id:{},productionCount:{}",id,productionCount);
		return preProductionService.updateActualProductionCountById(id, productionCount,UserInfoUtil.buildUpdateBy());
	}
}
