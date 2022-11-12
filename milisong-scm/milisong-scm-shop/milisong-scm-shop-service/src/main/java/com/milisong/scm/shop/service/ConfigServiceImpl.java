package com.milisong.scm.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.scm.shop.api.ConfigService;
import com.milisong.scm.shop.api.ShopBannerService;
import com.milisong.scm.shop.api.ShopInterceptService;
import com.milisong.scm.shop.api.ShopSingleConfigService;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigDto;
import com.milisong.scm.shop.dto.config.ShopSingleConfigDto;
import com.milisong.scm.shop.util.RedisConfigUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   11:44
 *    desc    :
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ShopInterceptService shopInterceptService;

    @Autowired
    private ShopBannerService shopBannerService;

    @Autowired
    private ShopSingleConfigService shopSingleConfigService;

    @Override
    public List<ShopInterceptConfigDto> queryInterceptConfigByShopId(Long shopId) {
        List<ShopInterceptConfigDto> list = RedisConfigUtil.getShopIncercept(shopId);
        return list;
    }

    @Override
    public List<ShopSingleConfigDto> queryShopSingleByShopId(Long shopId) {
        List<ShopSingleConfigDto> shopSingleConfig = RedisConfigUtil.getShopSingleConfig(shopId);
        return shopSingleConfig;
    }

    @Override
    public String getGlobalConfigByKey(String key) {
        Object globalConfig = RedisConfigUtil.getGlobalConfig(key);
        if(globalConfig == null ){
            return null;
        }else{
            return globalConfig.toString();
        }
    }

    @Override
    public ResponseResult<Boolean> copyConfig(Long srcId, ShopReqDto shopReqDto) {
        ResponseResult<Boolean> result1 = shopBannerService.copyConfig(srcId, shopReqDto);
        if(result1.getData() == null || result1.getData().equals(false)){
            return ResponseResult.buildFailResponse();
        }
        ResponseResult<Boolean> result2 = shopInterceptService.copyConfig(srcId, shopReqDto);
        if(result2.getData() == null || result2.getData().equals(false)){
            return ResponseResult.buildFailResponse();
        }
        ResponseResult<Boolean> result3 = shopSingleConfigService.copyConfig(srcId, shopReqDto);
        if(result3.getData() == null || result3.getData().equals(false)){
            return ResponseResult.buildFailResponse();
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<Boolean> syncShopConfig(Long shopId) {
        shopBannerService.syncShopConfig(shopId);
        shopInterceptService.syncShopConfig(shopId);
        shopSingleConfigService.syncShopConfig(shopId);
        return ResponseResult.buildSuccessResponse();
    }
}
