package com.milisong.breakfast.scm.configuration.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigDto;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigParam;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopReqDto;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   18:24
 *    desc    : 门店截单配置
 *    version : v1.0
 * </pre>
 */

public interface ShopInterceptService {

    ResponseResult<ShopInterceptConfigDto> saveOrUpdate(ShopInterceptConfigDto dto);

    ResponseResult<Pagination<ShopInterceptConfigDto>> getInterceptorConfig(ShopInterceptConfigParam param);

    ResponseResult<ShopInterceptConfigDto> getShopInterceptorById(Long id);

    List<ShopInterceptConfigDto> getInterceptorConfigByShopId(Long shopId);

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
