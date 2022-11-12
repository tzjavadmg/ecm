package com.milisong.pms.prom.api;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

/**
 * 早餐券
 *
 * @author sailor wang
 * @date 2018/12/11 9:57 AM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface BreakfastCouponService {

    /**
     * 领取新用户券
     *
     * @param queryParam
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/receiveNewCoupon"})
    ResponseResult<UserCouponDto> receiveNewCoupon(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 用户早餐券提示
     *
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/breakfastCouponTips"})
    ResponseResult<UserBreakfastCouponTips> breakfastCouponTips(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 可用优惠券列表
     *
     * @param queryParam
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/usableCoupons"})
    ResponseResult<MyUsableBreakfastCouponDto> usableCoupons(@RequestBody UserCouponQueryParam queryParam);


    /**
     * 结算优惠（券+积分+。。。。）
     *
     * @param queryParam
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/settleAccountsDiscount"})
    ResponseResult<MyUsableBreakfastCouponDto> settleAccountsDiscount(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 可用优惠券数量
     *
     * @param queryParam
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/usableCouponsCount"})
    ResponseResult<Integer> usableCouponsCount(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 我的券列表
     *
     * @param queryParam
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/myCoupon"})
    ResponseResult<Pagination<UserCouponDto>> myCoupon(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 定时扫描用户早餐券
     *
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/scanBreakfastCoupon"})
    ResponseResult<Boolean> scanBreakfastCoupon();

    /**
     * 发送指定早餐券
     *
     * @param queryParam
     * @return
     */
    @Deprecated
    @PostMapping({"/v1/BreakfastCouponService/sendBreakfastCoupon"})
    ResponseResult<Boolean> sendBreakfastCoupon(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 发送指定早餐券,升级版
     *
     * @param sendBreakCouponRequest
     * @return
     */
    @PostMapping({"/v1/BreakfastCouponService/batchSendBreakfastCoupon"})
    ResponseResult<Boolean> batchSendBreakfastCoupon(@RequestBody SendBreakCouponRequest sendBreakCouponRequest);

    /**
     * 用户券置为已使用
     *
     * @param queryParam
     * @return
     */
    @PostMapping(value = "/v1/BreakfastCouponService/useUserCoupon")
    ResponseResult<Boolean> useUserCoupon(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 用户券置为可用
     *
     * @param queryParam
     * @return
     */
    @PostMapping(value = "/v1/BreakfastCouponService/updateUserCouponUseful")
    ResponseResult<Boolean> updateUserCouponUseful(@RequestBody UserCouponQueryParam queryParam);

    /**
     * 下单后，分享并创建多人分享券实例
     *
     * @param couponRequest
     * @return
     */
    @PostMapping(value = "/v1/BreakfastCouponService/createMultiShareCoupon")
    ResponseResult createMultiShareCoupon(@RequestBody UserCouponRequest couponRequest) throws Exception;

    /**
     * 领取早餐多人分享券
     *
     * @param couponRequest
     * @return
     */
    @PostMapping(value = "/v1/BreakfastCouponService/recevieMultiShareCoupon")
    ResponseResult<UserCouponResponse> recevieMultiShareCoupon(@RequestBody UserCouponRequest couponRequest);

    /**
     * 最优折扣券
     *
     * @return
     */
    @PostMapping(value = "/v1/BreakfastCouponService/bestDiscount")
    ResponseResult<BigDecimal> bestDiscount();

}