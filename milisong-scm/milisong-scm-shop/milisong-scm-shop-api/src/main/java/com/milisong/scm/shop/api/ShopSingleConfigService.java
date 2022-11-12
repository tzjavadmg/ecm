package com.milisong.scm.shop.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.scm.shop.dto.config.MaxOutputDto;
import com.milisong.scm.shop.dto.config.ShopSingleConfigDto;
import com.milisong.scm.shop.dto.config.ShopSingleConfigParam;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   18:39
 *    desc    : 单个门店配置信息接口
 *    version : v1.0
 * </pre>
 */

public interface ShopSingleConfigService {

    ResponseResult<ShopSingleConfigDto> saveOrUpdate(ShopSingleConfigDto dto);

    ResponseResult<Pagination<ShopSingleConfigDto>> getShopSingleConfig(ShopSingleConfigParam param);

    ResponseResult<ShopSingleConfigDto> getByShopSingleConfigById(Long id);

    ResponseResult<MaxOutputDto> queryMaxOutput(Long shopId);

    ResponseResult<MaxOutputDto> updateMaxOutput(MaxOutputDto dto);

    List<ShopSingleConfigDto> getShopSingleConfigByShopId(Long shopId);

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

    /**
     * 同步所有配置到其他业务线MQ
     * @return
     */
    ResponseResult<Boolean> syncConfig(Long shopId);

}
