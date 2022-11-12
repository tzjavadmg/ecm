package com.milisong.pms.prom.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sailor wang
 * @date 2019/1/8 3:07 PM
 * @description
 */
@Data
public class SendBreakfastCouponParam {
    // 活动id
    private Long activityId;

    // 券类型
    private Byte type;

    // 券名称
    private String name;

    // 短信消息模板
    private String smsMsg;

    private List<BreakfastCoupon> coupons;

    private List<Long> userids;
}