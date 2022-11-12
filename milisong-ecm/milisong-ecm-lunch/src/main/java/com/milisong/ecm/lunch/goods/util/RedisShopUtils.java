package com.milisong.ecm.lunch.goods.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.goods.dto.GoodsCatalogDto;
import com.milisong.ecm.lunch.goods.dto.GoodsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.DateUtil;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/4   10:46
 *    desc    : 门店信息处理redis工具类
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Component
public class RedisShopUtils {

    @Autowired
    private RedisCache redisCache0;

    private static RedisCache redisCache;
    private static final Long DEFAULT_EXPIRE_TIME = 60 * 60 * 24 * 7L;

    @PostConstruct
    public void initRedisShopUtils() {
        redisCache = this.redisCache0;
    }

    private static final String SHOP_GOODS_PREFIX = "shop_goods:%s";

    private static String getkey(String format, String... params) {
        return String.format(format, params);
    }

    public static void cacheShopGoodsForDay(String categoryCode, String shopCode, Map<String, List<GoodsCatalogDto>> dto, List<GoodsDto> goodsList) {
    	String shopOnsaleDateKey = RedisKeyUtils.getShopOnsaleDate(shopCode);
    	String [] value = new String[dto.entrySet().size()];
    	int index = 0;
    	for (Map.Entry<String, List<GoodsCatalogDto>> entry : dto.entrySet()) {
        	String goodsDayKey = RedisKeyUtils.getShopGoodsDayKey(shopCode, categoryCode,DateUtil.parse(entry.getKey()));
            redisCache.set(goodsDayKey, JSON.toJSONString(entry.getValue()));
            redisCache.expireAt(goodsDayKey, DateUtils.addDays(DateUtil.parse(entry.getKey()), 1));
            value[index++] = entry.getKey();
        }
    	redisCache.sAdd(shopOnsaleDateKey, value);
        //构建商品基础信息
        for (GoodsDto goods : goodsList) {
        	String goodsKey = RedisKeyUtils.getGoodsInfoKey(goods.getCode());
            //存储商品基础信息
            redisCache.set(goodsKey, JSON.toJSONString(goods));
/*        	if (GoodsStatus.DETAIL_STATUS_USING.getValue() == goods.getDetailStatus()) {
                //存储商品基础信息
                redisCache.set(goodsKey, JSON.toJSONString(goods));
                redisCache.expireAt(goodsKey, DateUtils.addDays(goods.getEndDate(), 1));
        	} else if (GoodsStatus.DETAIL_STATUS_DOWNLINE.getValue() == goods.getDetailStatus()) {
        		redisCache.delete(goodsKey);
        	}*/

 
        }
    }

    public static void cacheShopGoodsMsg(Map<String, List<GoodsCatalogDto>> dto) {
        dto.entrySet().stream().forEach(o -> redisCache.setEx(getkey(SHOP_GOODS_PREFIX, o.getKey()), JSON.toJSONString(o.getValue()), DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS));
    }

    public static void cleanShopGoodsMsg(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        redisCache.delete(Arrays.asList(keys));
    }

    public static List<GoodsCatalogDto> getShopGoodsMsg(String shopCode, String categoryCode, String setOnTime) {
        String goodsDayKey = RedisKeyUtils.getShopGoodsDayKey(shopCode, categoryCode,setOnTime!=null?DateUtil.parse(setOnTime):null);
        //log.info("获取门店商品信息key{}",goodsDayKey);
        String s = redisCache.get(goodsDayKey);
        //log.info("获取商品信息 redis key:{},redis value:{}", goodsDayKey, s);
        return parseStr2Dto(s);
    }

    private static List<GoodsCatalogDto> parseStr2Dto(String val) {
        List<JSONObject> list = JSON.parseArray(val, JSONObject.class);
        List<GoodsCatalogDto> resultList = Lists.newArrayList();
        if (list == null || list.size() == 0) {
            return resultList;
        }
        list.stream().forEach(o -> {
            GoodsCatalogDto dto = new GoodsCatalogDto();
            dto.setCategoryCode(o.getString("categoryCode"));
            dto.setCategoryName(o.getString("categoryName"));
            dto.setWeight(o.getInteger("weight"));
            dto.setHasChild(o.getBoolean("hasChild"));
            if (o.get("children") != null) {
                dto.setChildren(parseStr2Dto(o.get("children").toString()));
            }
            List<GoodsDto> goodsDtos = JSON.parseArray(o.getString("goods"), GoodsDto.class);
            dto.setGoods(goodsDtos);
            resultList.add(dto);
        });
        return resultList;
    }
}
