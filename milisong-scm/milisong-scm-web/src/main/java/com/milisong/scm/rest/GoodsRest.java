package com.milisong.scm.rest;

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
import com.milisong.scm.goods.api.GoodsService;
import com.milisong.scm.goods.dto.GoodsDto;
import com.milisong.scm.goods.param.GoodsParam;
import com.milisong.scm.goods.param.GoodsQueryParam;
import com.milisong.upms.utils.UserInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品信息rest
 * @author youxia 2018年9月4日
 */
@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsRest {

	@Autowired(required=false)
	private GoodsService goodsService;

	/**
	 * 分页查询商品信息
	 * @param param
	 * @return
	 * youxia 2018年9月4日
	 */
	@PostMapping("/get-goods-pageInfo")
	public ResponseResult<Pagination<GoodsDto>> getGoodsPageInfo(@RequestBody GoodsQueryParam param) {
		try {
			log.info("分页查询商品信息：param={}", JSONObject.toJSONString(param));
			return goodsService.getGoodsPageInfo(param);
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
	@GetMapping("/get-goods-details-byId")
	public ResponseResult<GoodsDto> getGoodsDetailsById(@RequestParam("goodsId") Long goodsId) {
		try {
			log.info("根据id查询商品明细：goodsId={}", goodsId);
			return goodsService.getGoodsDetailsById(goodsId);
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
	@PostMapping("/save-goodsInfo")
	public ResponseResult<String> saveGoodsInfo(@RequestBody GoodsParam goodsParam) {
		try {
			log.info("保存商品信息：goodsParam={}", JSONObject.toJSONString(goodsParam));
			goodsParam.setUpdateBy(UserInfoUtil.buildUpdateBy());
			return goodsService.saveGoodsInfo(goodsParam);
		} catch (Exception e) {
			log.error("保存商品信息出现异常", e);
			throw e;
		}
	}
	
	@GetMapping("/do-task")
	public ResponseResult<String> doTask() {
		try {
			return goodsService.updateGoodsStatus();
		} catch (Exception e) {
			log.error("保存商品信息出现异常", e);
			throw e;
		}
	}

}
