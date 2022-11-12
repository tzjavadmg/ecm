package com.milisong.scm.shop.api;

import java.util.List;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigDto;
import com.milisong.scm.shop.dto.config.ShopSingleConfigDto;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   11:43
 *    desc    : 配置业务接口
 *    version : v1.0
 * </pre>
 */

public interface ConfigService {

    public List<ShopInterceptConfigDto> queryInterceptConfigByShopId(Long shopId);

    public List<ShopSingleConfigDto> queryShopSingleByShopId(Long shopId);

    public String getGlobalConfigByKey(String key);

    /**
     * 拷贝配置
     * @param srcId 拷贝源门店id
     * @param shopReqDto 新增门店的信息
     * @return
     */
    ResponseResult<Boolean> copyConfig(Long srcId, ShopReqDto shopReqDto);

    /**
     * 同步门店配置到其他业务线MQ
     * @param shopId
     * @return
     */
    ResponseResult<Boolean> syncShopConfig(Long shopId);
}
