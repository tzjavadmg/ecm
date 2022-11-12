package com.milisong.oms.service.promotion;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.oms.api.PromotionLockerService;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ Description：
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/13 21:49
 */
@Slf4j
@RestController
public class PromotionLockerServiceImpl implements PromotionLockerService {

    @Override
    public ResponseResult<List<Long>> getLockerRedPacket(@RequestBody List<Long> redPacketIdList) {
        try {
            if (CollectionUtils.isEmpty(redPacketIdList)) {
                return ResponseResult.buildFailResponse();
            }
            List<Long> lockedRedPacketIds = redPacketIdList.stream().filter(redPacketId -> StringUtils.isNotEmpty(RedisCache.get(OrderRedisKeyUtils.getRedPacketLockKey(redPacketId)))).collect(Collectors.toList());
            return ResponseResult.buildSuccessResponse(lockedRedPacketIds);
        } catch (Exception e) {
            return ResponseResult.buildFailResponse();
        }
    }

    @Override
    public ResponseResult<List<Long>> getLockerCoupon(@RequestBody List<Long> couponIdList) {
        try {
            if (CollectionUtils.isEmpty(couponIdList)) {
                return ResponseResult.buildFailResponse();
            }
            List<Long> lockedCouponIds = couponIdList.stream().filter(couponId -> StringUtils.isNotEmpty(RedisCache.get(OrderRedisKeyUtils.getCouponLockKey(couponId)))).collect(Collectors.toList());
            return ResponseResult.buildSuccessResponse(lockedCouponIds);
        } catch (Exception e) {
            return ResponseResult.buildFailResponse();
        }
    }
}
