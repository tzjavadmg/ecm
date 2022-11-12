package com.milisong.oms.mq;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/20 11:04
 */
@Getter
@Setter
public class PaymentMessage {

    private Long messageId;

    private Long userId;

    /**
     * 订单类型，即业务线类型
     */
    private Byte orderType;

    private Byte orderSource;

    private Byte orderMode;

    private String shopCode;

    private String openId;

    private Long orderId;
    /**
     * 红包ID
     */
    private Long redPacketId;
    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 抵扣使用积分
     */
    private Long deductionPoints;

    /**
     * 订单获得积分
     */
    private Long acquirePoints;

    private BigDecimal amount;

    private Long deliveryAddressId;

    private String errorCode;

    private String errorDescription;

    private byte status;
    @Deprecated
    private boolean paySuccess;
}
