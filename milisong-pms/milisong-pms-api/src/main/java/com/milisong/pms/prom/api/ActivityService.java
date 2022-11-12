package com.milisong.pms.prom.api;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.ActivityRedPacketDto;
import com.milisong.pms.prom.dto.ActivityQueryParam;
import com.milisong.pms.prom.dto.MultiBreakfastCouponConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 活动接口
 *
 * @author sailor wang
 * @date 2018/9/13 下午1:44
 * @description
 */
@FeignClient("milisong-pms-service")
public interface ActivityService {
    /**
     * 定时扫描活动状态
     *
     * @return
     */
    @PostMapping(value = "/v1/ActivityService/scanActivityStatus")
    ResponseResult scanActivityStatus(@RequestParam("businessLine")Byte businessLine);

    /**
     * 查询当前的红包
     *
     * @return
     */
    @PostMapping(value = "/v1/ActivityService/currentActivityRedPacket")
    ResponseResult<ActivityRedPacketDto> currentActivityRedPacket(@RequestBody ActivityQueryParam queryParam);

    /**
     * 活动保存入口
     *
     * @param activityRedPacket
     * @return
     */
    @PostMapping(value = "/v1/ActivityService/saveOrUpdateActivityRedPacket")
    ResponseResult<Boolean> saveOrUpdateActivityRedPacket(@RequestBody ActivityRedPacketDto activityRedPacket);

    /**
     * 查询红包活动列表
     *
     * @param queryParam
     * @return
     */
    @PostMapping(value = "/v1/ActivityService/queryActivityRedPacketList")
    ResponseResult<Pagination<ActivityRedPacketDto>> queryActivityRedPacketList(@RequestBody ActivityQueryParam queryParam);

    /**
     * 获取当前券活动配置
     *
     * @param queryParam
     * @return
     */
    @PostMapping(value = "/v1/ActivityService/currentActivityCouponConfig")
    ResponseResult<MultiBreakfastCouponConfig> currentActivityCouponConfig(@RequestBody ActivityQueryParam queryParam);
}