package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/12/11 1:23 PM
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBreakfastCouponTips implements Serializable {
    private static final long serialVersionUID = -1484063659089738403L;

    private String tips;
    private Integer count;
    private List<UserCouponDto> coupons;

}