package com.milisong.ecm.breakfast.goods.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.breakfast.dto.GoodsDto;
import com.milisong.ecm.breakfast.dto.GoodsMqDto;
import com.milisong.ecm.breakfast.goods.api.ShopService;
import com.milisong.ecm.common.util.RedisKeyUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/3   11:42
 *    desc    : 门店业务实现类
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Service
@RestController
public class ShopServiceImpl implements ShopService {
    @Override
    public ResponseResult<GoodsDto> getGoodsInfo(@RequestParam("goodsCode") String goodsCode) {
    	log.info("获取门店商品价格信息,goodsCode={}",goodsCode);
    	String goodsBasicKey = RedisKeyUtils.getGoodsBasicKey(goodsCode);
    	String goodsBasic = RedisCache.get(goodsBasicKey);
    	GoodsDto goods = new GoodsDto();
    	if (goodsBasic != null) {
    		GoodsMqDto goodsMqDto = JSONObject.parseObject(goodsBasic, GoodsMqDto.class);
    		BeanMapper.copy(goodsMqDto,goods);
    		
    	}
        return ResponseResult.buildSuccessResponse(goods);
    }
}
