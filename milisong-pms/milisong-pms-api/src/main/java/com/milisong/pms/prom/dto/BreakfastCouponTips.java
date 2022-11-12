package com.milisong.pms.prom.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BreakfastCouponTips {
    // 买几天
    private int days;

    // 打几折
    private BigDecimal discount;

    // 有效天数
    private int valid;

    // 使用规则
    private String useRule;

    // 开始时间
    private Date startDate;

    // 结束时间
    private Date endDate;
}