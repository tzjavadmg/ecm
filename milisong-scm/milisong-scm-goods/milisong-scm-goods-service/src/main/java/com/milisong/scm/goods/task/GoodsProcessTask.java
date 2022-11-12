package com.milisong.scm.goods.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.goods.api.GoodsService;
//import com.milisong.scm.stock.api.ShopOnsaleGoodsStockService;
import com.milisong.scm.stock.api.ShopOnsaleGoodsStockService;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品定时任务
 * @author youxia 2018年9月4日
 */
@Slf4j
@RestController
@RequestMapping("/goods")
@Component
@EnableScheduling
public class GoodsProcessTask {
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ShopOnsaleGoodsStockService shopOnsaleGoodsStockService;
	
	@GetMapping("/task")
	public void updateGoodsStatus() {
		log.info("更新商品状态begin");
		ResponseResult<String> result = goodsService.updateGoodsStatus();
		log.info("更新商品状态结果：result={}", JSONObject.toJSONString(result));
		log.info("更新商品状态end");
		log.info("初始化商品可售数量begin");
		shopOnsaleGoodsStockService.initOnsaleGoodsStockV2();
		log.info("初始化商品可售数量end");		
	}

}
