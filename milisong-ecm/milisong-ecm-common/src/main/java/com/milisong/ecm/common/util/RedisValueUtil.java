package com.milisong.ecm.common.util;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaolei
 * @program mili
 * @date 2019/5/24 15:33
 */
@Slf4j
public class RedisValueUtil {

    /**
     * 根据goodsCode从redis中获取goodsName
     * @param goodsCode
     * @return
     */
    public static String getGoodsName(String goodsCode) {
        String goodsBasicKey = RedisKeyUtils.getGoodsBasicKey(goodsCode);
        String goodsBasic = RedisCache.get(goodsBasicKey);
        if (StringUtils.isBlank(goodsBasic)) {
            log.info("getGoodsDto 商品为空, goodsCode -> {}", goodsCode);
            return null;
        }
        log.info("商品信息 goodsBasic -> {}", goodsBasic);
        //商品名称
        JSONObject jsonObject = JSONObject.parseObject(goodsBasic);
        return jsonObject.getString("name");
    }

}
