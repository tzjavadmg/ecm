package com.milisong.scm.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.goods.api.GoodsSalableQuantityLunchService;
import com.milisong.scm.goods.dto.GoodsSalableQuantityLunchDto;
import com.milisong.scm.goods.param.GoodsSalableQuantityLunchParam;
import com.milisong.scm.goods.param.GoosSalableLunchParam;
import com.milisong.upms.utils.UserInfoUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年12月5日---下午1:58:53
*
*/
@Api(tags = "商品可售量管理")
@Slf4j
@RestController
@RequestMapping("/salable-quantity")
public class GoodsSalableQuantityLunchRest {

	
	@Autowired
	private GoodsSalableQuantityLunchService shopGoodsSalableQuantityService;
	
	@ApiOperation("分页查询商品可售量")
	@PostMapping("/get-shop-goods-salable-by")
	public ResponseResult<Pagination<GoodsSalableQuantityLunchDto>> getShopGoodsSalableQuantityBy(@RequestBody GoosSalableLunchParam goosSalableParam){
		return shopGoodsSalableQuantityService.getByShopIdAndDate(goosSalableParam);
	}
	
	@ApiOperation("修改商品可售量")
	@PostMapping("/update")
	public ResponseResult<String> update(@RequestBody GoodsSalableQuantityLunchParam shopGoodsSalableQuantityParam){
		String updateUser = UserInfoUtil.buildUpdateBy();
		shopGoodsSalableQuantityParam.setOperator(updateUser);
		if(null == shopGoodsSalableQuantityParam.getAvailableVolume()) {
			return ResponseResult .buildSuccessResponse();
		}
		return shopGoodsSalableQuantityService.update(shopGoodsSalableQuantityParam);
	}
}
