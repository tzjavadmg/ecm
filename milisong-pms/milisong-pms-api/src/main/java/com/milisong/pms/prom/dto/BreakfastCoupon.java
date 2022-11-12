package com.milisong.pms.prom.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BreakfastCoupon {

    //券名称
    private String name;

    // 买几天
    private int days;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 打几折
      */
    private BigDecimal discount;

    /**
     * 有效天数
     */
    private int valid;

    /**
     * 使用规则
     */
    private String rule;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 券类型
     */
    private Byte type;
}