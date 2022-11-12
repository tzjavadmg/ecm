package com.milisong.pms.breakfast.web;

import com.milisong.pms.breakfast.service.RestGroupBuyService;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.SendCouponRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sailor wang
 * @date 2018/11/23 10:41 AM
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/promotion/task/")
public class PromotionTaskRest {

    @Autowired
    BreakfastCouponService breakfastCouponService;

    @Autowired
    SendCouponRecordService sendCouponRecordService;

    @Autowired
    RestGroupBuyService restGroupBuyService;

    /**
     * 定时扫描用户早餐券
     */
    //@Scheduled(cron = "0 0/1 * * * ?")
    @GetMapping("scan-user-coupon")
    public void scanUserCoupon() {
        log.info("定时扫描用户早餐券");
        breakfastCouponService.scanBreakfastCoupon();
    }

    @GetMapping("send-coupon")
    public void scheduleSendCoupon() {
        log.info("定时扫描用户早餐券");
        sendCouponRecordService.scheduleSendCoupon();
    }

    /**
     * 拼团退款
     */
    @GetMapping("refund-group-buy")
    public void cancelGroupBuy(){
        log.info("定时扫描用户早餐券");
        restGroupBuyService.refundGroupBuy();
    }
}