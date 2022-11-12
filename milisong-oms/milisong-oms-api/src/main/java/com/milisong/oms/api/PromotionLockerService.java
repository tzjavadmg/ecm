package com.milisong.oms.api;

import com.farmland.core.api.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ Description：
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/13 21:45
 */
@FeignClient("milisong-oms-service")
public interface PromotionLockerService {

    /**
     *
     * @param redPacketIdList
     * @return
     */
    @PostMapping(value = "/v1/PromotionLockerService/getLockerRedPacket")
    ResponseResult<List<Long>> getLockerRedPacket(@RequestBody List<Long> redPacketIdList);

    /**
     *
     * @param couponIdList
     * @return
     */
    @PostMapping(value = "/v1/PromotionLockerService/getLockerCoupon")
    ResponseResult<List<Long>> getLockerCoupon(@RequestBody List<Long> couponIdList);
}
