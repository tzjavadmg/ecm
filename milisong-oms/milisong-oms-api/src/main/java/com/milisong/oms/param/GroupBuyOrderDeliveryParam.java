package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 15:47
 */
@Getter
@Setter
public class GroupBuyOrderDeliveryParam {

    /**
     * 配送时间
     */
    private Date deliveryDate;

    /**
     * 配送时间段-开始时间
     */
    private String startTime;

    /**
     * 配送时间段-结束时间
     */
    private String endTime;
    /**
     * 配送费原价
     */
    private BigDecimal deliveryOriginalPrice;
    /**
     * 配送费实价
     */
    private BigDecimal deliveryActualPrice;
    /**
     * 预订哪几种饭
     */
    private List<GroupBuyOrderDeliveryGoodsParam> deliveryGoods;

    private transient Long orderId;
    private transient Long deliveryId;
}
