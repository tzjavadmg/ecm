package com.milisong.breakfast.scm.configuration.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.configuration.dto.ShopBannerDto;
import com.milisong.breakfast.scm.configuration.dto.ShopBannerParam;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopReqDto;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   18:03
 *    desc    : 门店banner图业务接口
 *    version : v1.0
 * </pre>
 */

public interface ShopBannerService {

    ResponseResult<ShopBannerDto> saveOrUpdate(ShopBannerDto dto);

    ResponseResult<Pagination<ShopBannerDto>> getShopBanner(ShopBannerParam param);

    ResponseResult<ShopBannerDto> getShopBannerById(Long id);

    List<ShopBannerDto> getShopBannerByShopId(Long shopId);

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
