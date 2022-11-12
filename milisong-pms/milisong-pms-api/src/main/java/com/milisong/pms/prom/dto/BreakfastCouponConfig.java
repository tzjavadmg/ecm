package com.milisong.pms.prom.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/12/11 1:39 PM
 * @description
 */
@Data
public class BreakfastCouponConfig {
    // 活动id
    private Long activityId;

    // 券类型
    private Byte type;

    private String name;

    // 短信消息模板
    private String smsMsg;

    private List<BreakfastCoupon> coupons;

}
