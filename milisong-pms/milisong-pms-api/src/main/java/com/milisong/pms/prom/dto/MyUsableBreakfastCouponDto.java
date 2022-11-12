package com.milisong.pms.prom.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/12/12 2:06 PM
 * @description
 */
@Data
public class MyUsableBreakfastCouponDto {
    // 可用券列表
    private List<UserCouponDto> usableCoupons;

    // 可用但不满足规则券列表
    private List<UserCouponDto> unusableCoupons;

    // 总积分
    private Integer totalPoints;

    // 结单页,有不可用的券时，显示该标识语
    private String unusableLabel;

}