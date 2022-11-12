package com.mili.oss.api;

import java.util.List;

import com.farmland.core.api.ResponseResult;
import com.mili.oss.dto.config.ShopInterceptConfigDto;
import com.mili.oss.dto.config.ShopSingleConfigDto;

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

    /**
     * HTTP请求获取早餐配置信息
     * @param shopId
     * @return
     */
    public List<ShopInterceptConfigDto> queryHttpInterceptConfigByShopId(Long shopId);
}
