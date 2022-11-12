package com.milisong.pms.breakfast.web;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pms.breakfast.service.RestBreakfastPromotionService;
import com.milisong.pms.prom.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:32
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/breakfast/")
public class PromotionBreakfastRest {

    @Autowired
    RestBreakfastPromotionService promotionService;

    /**
     * 发起多人分享券
     * @return
     */
    @GetMapping("create-multisharecoupon/{orderid}")
    ResponseResult createMultiShareCoupon(@PathVariable("orderid") Long orderid){
        return promotionService.createMultiShareCoupon(orderid);
    }

    /**
     * 领取多人分享券
     * @return
     */
    @GetMapping("receive-multisharecoupon/{orderid}")
    ResponseResult<UserCouponResponse> recevieMultiShareCoupon(@PathVariable("orderid") Long orderid){
        return promotionService.recevieMultiShareCoupon(orderid);
    }

    /**
     * 多人分享券详情
     * @return
     */
    @GetMapping("multisharecoupon-info")
    ResponseResult<MultiBreakfastCouponConfig> multiShareCouponInfo(){
        return promotionService.multiShareCouponInfo();
    }

    /**
     * 券引导弹层
     * @return
     */
    @GetMapping("coupon-toast")
    ResponseResult<UserBreakfastCouponTips> couponToast(){
        return promotionService.breakfastCouponTips();
    }

    /**
     * 结算页，可用券列表
     * @return
     */
    @PostMapping("usable-coupons")
    ResponseResult<MyUsableBreakfastCouponDto> usableCoupons(@RequestBody UserCouponQueryParam queryParam){
        return promotionService.usableCoupons(queryParam);
    }

    /**
     * 结算优惠
     *
     * @param queryParam
     * @return
     */
    @PostMapping("settle-accounts-discount")
    ResponseResult<MyUsableBreakfastCouponDto> settleAccountsDiscount(@RequestBody UserCouponQueryParam queryParam){
        return promotionService.settleAccountsDiscount(queryParam);
    }

    /**
     * 可用券数量
     *
     * @return
     */
    @GetMapping("usable-coupon-count")
    ResponseResult<Integer> usableRedpacketCount(){
        return promotionService.usableCouponsCount();
    }

    /**
     * 我的优惠券列表
     *
     * @param queryParam
     * @return
     */
    @PostMapping("my-coupon")
    ResponseResult<Pagination<UserCouponDto>> myCoupon(@RequestBody UserCouponQueryParam queryParam) {
        return promotionService.myCoupon(queryParam);
    }

    /**
     * 批量推送早餐券
     *
     * @param queryParam
     * @return
     */
    @PostMapping("batch-send-coupon")
    public ResponseResult<Boolean> sendUserCoupon(@RequestBody UserCouponQueryParam queryParam){
        return promotionService.sendUserCoupon(queryParam);
    }

}