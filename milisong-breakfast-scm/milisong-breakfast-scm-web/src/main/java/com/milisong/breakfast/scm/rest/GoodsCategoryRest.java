package com.milisong.breakfast.scm.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.goods.api.GoodsCategoryService;
import com.milisong.breakfast.scm.goods.dto.GoodsCategoryInfoDto;
import com.milisong.breakfast.scm.goods.param.GoodsCategoryParam;
import com.milisong.breakfast.scm.goods.param.GoodsCategoryQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年12月7日---上午12:35:43
*
*/
@Api(tags = "类目管理")
@Slf4j
@RestController
@RequestMapping("/goods-category")
public class GoodsCategoryRest {

	@Autowired
	private GoodsCategoryService goodsCategoryService;
	
	@ApiOperation("根据类型查询类目详情")
	@GetMapping("/get-all")
	public ResponseResult<List<GoodsCategoryInfoDto>> getAll(@RequestParam("type") @ApiParam("分类类型(1单品，2套餐)") byte type) {
		return goodsCategoryService.getAll(type);
	}
	
	@ApiOperation("根据id查询类目详情")
	@GetMapping("/get-by-id")
	public ResponseResult<GoodsCategoryInfoDto> getGoodsCategoryById(@RequestParam("id") @ApiParam("id") Long id) {
		return goodsCategoryService.getGoodsCategoryById(id);
	}
	
	@ApiOperation("查询类目列表")
	@PostMapping("/get-category-list")
	public ResponseResult<Pagination<GoodsCategoryInfoDto>> getGoodsCategoryPageInfo(@RequestBody @ApiParam("类目查询条件") GoodsCategoryQueryParam param) {
		return goodsCategoryService.getGoodsCategoryPageInfo(param);
	}
	
	@ApiOperation("保存类目信息")
	@PostMapping("/save-goodsCategory")
	public ResponseResult<String> saveGoodsCategory(@RequestBody @ApiParam("保存类目信息参数") GoodsCategoryParam param) {
		return goodsCategoryService.saveGoodsCategory(param);
	}
}
