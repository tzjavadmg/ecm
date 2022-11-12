package com.milisong.dms.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.dms.api.ShopService;
import com.milisong.dms.dto.httpdto.ScmUrlDto;
import com.milisong.dms.dto.httpdto.ShopDto;
import com.milisong.dms.util.ShunfengRedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaozhonghui
 * @Description 门店http请求接口
 * @date 2018-12-11
 */
@Slf4j
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ScmUrlDto scmUrlDto;

    @Override
    public List<ShopDto> queryShopList() {
        log.info("http请求门店接口,请求路径:{}", scmUrlDto.getQueryAllShopUrl());
        ResponseResult result = restTemplate.postForObject(scmUrlDto.getQueryAllShopUrl(), null, ResponseResult.class);
        log.info("http请求scm集单接口接口返回:{}", JSONObject.toJSONString(result));
        if (result.isSuccess()) {
            List<ShopDto> orderSetDetailDtos = JSONArray.parseArray(JSONObject.toJSONString(result.getData()), ShopDto.class);
            return orderSetDetailDtos;
        } else {
            log.error(result.getCode() + ":" + result.getMessage());
            return null;
        }
    }

    @Override
    public ShopDto getShopById(String shopId) {
        log.info("http请求门店接口,请求路径:{}", scmUrlDto.getQueryShopByIdUrl());
        Map<String, Object> map = new HashMap<>();
        map.put("id", shopId);
        ResponseResult result = restTemplate.getForObject(scmUrlDto.getQueryShopByIdUrl(),ResponseResult.class, map);
        log.info("http请求scm集单接口接口返回:{}", JSONObject.toJSONString(result));
        if (result.isSuccess()) {
            return JSONObject.parseObject(JSONObject.toJSONString(result.getData()), ShopDto.class);
        } else {
            log.error(result.getCode() + ":" + result.getMessage());
            return null;
        }
    }

    @Override
    public void syncShopInfo(ShopDto shopDto) {
        String shopKey = ShunfengRedisKeyUtil.getShopKey();
        RedisCache.hPut(shopKey, shopDto.getId().toString(), JSONObject.toJSONString(shopDto));
    }

}
