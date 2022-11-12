package com.milisong.breakfast.scm.configuration.api;

import java.util.Date;
import java.util.List;

import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigDto;
import com.milisong.breakfast.scm.configuration.dto.ShopSingleConfigDto;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopReqDto;

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
     * @return*/

    ResponseResult<Boolean> copyConfig(Long srcId, ShopReqDto shopReqDto);

    /**
     * 同步门店配置到其他业务线MQ
     * @param shopId
     * @return
     */
    ResponseResult<Boolean> syncShopConfig(Long shopId);

    /**
     * 获取门店派单时间
     * @param shopId
     * @return
     */
    ResponseResult<Date> getShopInterceptDispatchTime(Long shopId);

    /**
     * 获取门店派单时间,根据配送时间段
     * @param shopId
     * @param beginTime
     * @param endTime
     * @return
     */
    ResponseResult<Date> getShopInterceptDispatchTime(Long shopId,Date beginTime, Date endTime);
}
