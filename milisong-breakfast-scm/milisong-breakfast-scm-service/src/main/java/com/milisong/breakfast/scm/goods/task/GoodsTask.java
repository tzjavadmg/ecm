package com.milisong.breakfast.scm.goods.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.minlog.Log;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.common.properties.SystemProperties;
import com.milisong.breakfast.scm.goods.api.GoodsService;
import com.milisong.breakfast.scm.goods.domain.Goods;
import com.milisong.breakfast.scm.goods.domain.GoodsCategory;
import com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample;
import com.milisong.breakfast.scm.goods.domain.GoodsExample;
import com.milisong.breakfast.scm.goods.mapper.GoodsCategoryMapper;
import com.milisong.breakfast.scm.goods.mapper.GoodsMapper;
import com.milisong.breakfast.scm.goods.mq.GoodsCategoryDto;
import com.milisong.breakfast.scm.goods.utils.MqProducerGoodsUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/goods")
@RefreshScope
@Slf4j
public class GoodsTask {
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	
	@Resource
    private SystemProperties systemProperties;
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Autowired
	private GoodsService goodsService;
	/**
	 * 同步类目
	 */
	@GetMapping("/sync-category")
	public void categoryMqSync() {
		GoodsCategoryExample example = new GoodsCategoryExample();
		List<GoodsCategory> list = goodsCategoryMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(list)) {
			list.forEach(item -> {
				item.setPicture(systemProperties.getFileOss().getViewUrl().concat("/6/").concat(item.getPicture()));
			});
			MqProducerGoodsUtil.sendGoodsCategoryMsg(BeanMapper.mapList(list, GoodsCategoryDto.class));
		}
	}
	
	/**
	 * 检查新品有效期
	 */
	@GetMapping("/sync-goods")
	public void goodsMqSync() {
		//检查是否有存在过期的新品
		log.info("检查是否有存在过期的新品");
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andIsNewGoodsEqualTo(true).andNewGoodsExpiredTimeLessThan(new Date());
		List<Goods> listResult = goodsMapper.selectByExample(goodsExample);
		if(!CollectionUtils.isEmpty(listResult)) {
			log.info("存在需要更新的过期商品信息,{}",JSON.toJSONString(listResult));
			for (Goods goods : listResult) {
				goods.setIsNewGoods(false);
				goodsExample.clear();
				goodsExample.createCriteria().andIdEqualTo(goods.getId());
				goodsMapper.updateByExample(goods, goodsExample);
				goodsService.senGoodsInfoMq(goods.getId());
			}
		}
	}
}
