package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sailor wang
 * @date 2019/1/13 10:37 AM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface SendCouponRecordService {

    @PostMapping(value = "/v1/SendCouponRecordService/getById")
    ResponseResult<SendCouponSearchConditionDto> getById(@RequestParam("sendCouponRecordId") Long sendCouponRecordId);

    /**
     * 查询券发送记录
     *
     * @param recordRequest
     * @return
     */
    @PostMapping("/v1/SendCouponRecordService/querySendCouponRecord")
    ResponseResult<Pagenation<SendCouponRecordDto>> querySendCouponRecord(@RequestBody SendCouponRecordRequest recordRequest);

    /**
     * 查看发送券流水
     *
     * @param recordWaterRequest
     * @return
     */
    @PostMapping("/v1/SendCouponRecordService/querySendCouponWater")
    ResponseResult<Pagenation<SendCouponWaterDto>> querySendCouponWater(@RequestBody SendCouponRecordWaterRequest recordWaterRequest);

    /**
     * 定时调度发券
     *
     * @return
     */
    @PostMapping("/v1/SendCouponRecordService/scheduleSendCoupon")
    ResponseResult<Boolean> scheduleSendCoupon();
}
