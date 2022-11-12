package com.milisong.breakfast.scm.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.goods.api.ShopGoodsSalableQuantityService;
import com.milisong.breakfast.scm.goods.dto.ShopGoodsSalableQuantityDto;
import com.milisong.breakfast.scm.goods.param.GoosSalableParam;
import com.milisong.breakfast.scm.goods.param.ShopGoodsSalableQuantityParam;
import com.milisong.breakfast.scm.properties.OssUrlProperties;
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
public class ShopGoodsSalableQuantityRest {

	
	@Autowired
	private ShopGoodsSalableQuantityService shopGoodsSalableQuantityService;
	
	@Autowired
	private OssUrlProperties ossUrlProperties;
	
	@ApiOperation("分页查询商品可售量")
	@PostMapping("/get-shop-goods-salable-by")
	public ResponseResult<Pagination<ShopGoodsSalableQuantityDto>> getShopGoodsSalableQuantityBy(@RequestBody GoosSalableParam goosSalableParam){
		return shopGoodsSalableQuantityService.getByShopIdAndDate(goosSalableParam,ossUrlProperties.getGoodsCount());
	}
	
	@ApiOperation("修改商品可售量")
	@PostMapping("/update")
	public ResponseResult<String> update(@RequestBody ShopGoodsSalableQuantityParam shopGoodsSalableQuantityParam){
		String updateUser = UserInfoUtil.buildUpdateBy();
		shopGoodsSalableQuantityParam.setOperator(updateUser);
		return shopGoodsSalableQuantityService.update(shopGoodsSalableQuantityParam,ossUrlProperties.getGoodsCount());
	}
}
