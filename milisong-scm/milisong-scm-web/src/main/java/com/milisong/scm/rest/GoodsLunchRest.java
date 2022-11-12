package com.milisong.scm.rest;

import java.util.List;

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
import com.milisong.scm.goods.api.GoodsLunchService;
import com.milisong.scm.goods.dto.GoodsLunchDto;
import com.milisong.scm.goods.param.GoodsLunchParam;
import com.milisong.scm.goods.param.GoodsLunchQueryParam;
import com.milisong.upms.utils.UserInfoUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author benny
 *
 */
@Api(tags = "商品管理（NEW）")
@Slf4j
@RestController
@RequestMapping("/goods-lunch")
public class GoodsLunchRest {
	@Autowired(required=false)
	private GoodsLunchService goodsService;

	/**
	 * 分页查询商品信息
	 * @param param
	 * @return
	 * youxia 2018年9月4日
	 */
	@ApiOperation("分页查询商品信息")
	@PostMapping("/get-pageInfo")
	public ResponseResult<Pagination<GoodsLunchDto>> getGoodsPageInfo(@RequestBody GoodsLunchQueryParam param) {
		try {
			log.info("分页查询商品信息：param={}", JSONObject.toJSONString(param));
			return goodsService.getGoodsLunchPageInfo(param);
		} catch (Exception e) {
			log.error("调用商品分页查询出现异常", e);
			throw e;
		}
	}
	
 	/**
	 * 根据id查询商品明细
	 * @param goodsId 商品id
	 * @return
	 * youxia 2018年9月2日
	 */
	@ApiOperation("根据id查询商品明细")
	@GetMapping("/get-details-byId")
	public ResponseResult<GoodsLunchDto> getGoodsDetailsById(@RequestParam("goodsId") Long goodsId) {
		try {
			log.info("根据id查询商品明细：goodsId={}", goodsId);
			return goodsService.getGoodsLunchDetailsById(goodsId);
		} catch (Exception e) {
			log.error("调用商品明细查询出现异常", e);
			throw e;
		}
	}

	/**
	 * 保存商品信息
	 * @return
	 * youxia 2018年9月2日
	 */
	@ApiOperation("保存商品信息")
	@PostMapping("/save-goodsInfo")
	public ResponseResult<String> saveGoodsInfo(@RequestBody GoodsLunchParam goodsParam) {
		try {
			log.info("保存商品信息：goodsParam={}", JSONObject.toJSONString(goodsParam));
			goodsParam.setUpdateBy(UserInfoUtil.buildUpdateBy());
			return goodsService.saveGoodsLunchInfo(goodsParam);
		} catch (Exception e) {
			log.error("保存商品信息出现异常", e);
			throw e;
		}
	} 
	
	
	@ApiOperation("档期可售商品查询")
	@GetMapping("/get-useing-goods-by-schedule")
	public ResponseResult<List<GoodsLunchDto>> getUseingGoodsBySchedule(@RequestParam("year")Integer year,@RequestParam("weekOfYear")Integer weekOfYear,@RequestParam("shopId")Long shopId){
		return goodsService.getGoodsLunchBySchedule(year, weekOfYear, shopId);
	}
	
	/**
	 * 根据商品名称模糊查询
	 * @param param
	 * @return
	 * youxia 2018年9月4日
	 */
	@ApiOperation("根据商品名称模糊查询")
	@GetMapping("/get-info-by-name")
	public ResponseResult<List<GoodsLunchDto>> getInfoByName(@RequestParam("name") String name) {
		try {
			log.info("根据商品名称模糊查询：param={}",name);
			return goodsService.getInfoByName(name);
		} catch (Exception e) {
			log.error("调用商品分页查询出现异常", e);
			throw e;
		}
	}
}
