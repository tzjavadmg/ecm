package com.milisong.oms.mq;

import lombok.Getter;
import lombok.Setter;

/**
 * @ Description：取消订单时的MQ消息
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/1/3 17:14
 */
@Getter
@Setter
public class CancelMessage {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 订单类型
     */
    private Byte orderType;

    /**
     * 订单使用抵扣积分
     */
    private Integer deductionPoints;
}
