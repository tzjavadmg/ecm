package com.milisong.ecm.breakfast.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 早餐-商品档期主表
 */
@Getter
@Setter
public class GoodsScheduleMqDto {
    /**
     * 
     */
    private Long id;

    /**
     * 门店d
     */
    private Long shopId;

    /**
     * 门店code
     */
    private String shopCode;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 年份
     */
    private Integer year;

    /**
     * W多少周
     */
    private Integer weekOfYear;

    /**
     * 档期日期
     */
    private Date scheduleDate;

    /**
     * 周几
     */
    private Integer week;

    /**
     * 是可可售 1可售 0不可售
     */
    private Boolean status;
 
}