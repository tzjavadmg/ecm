package com.milisong.oms.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 退款流水表。用于记录微信退款流水。
 * 1.调用微信退款接口时，如果返回错误，记录失败流水。如果成功则转到步骤2.
 * 2.微信退款回调时，根据返回结果，记录退款流水。
 */
@Getter
@Setter
public class RefundWater {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 退款单ID
     */
    private Long refundId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 微信openid
     */
    private String openId;

    /**
     * 门店编号
     */
    private String shopCode;

    /**
     * 联系人姓名
     */
    private String realName;

    /**
     * 联系人性别
     */
    private Byte sex;

    /**
     * 联系人手机号
     */
    private String mobileNo;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorDescription;

    /**
     * 订单类型：0:早餐;1:午餐;2:下午茶;3:晚餐;4:夜宵
     */
    private Byte orderType;

    /**
     * 下单模式：0:普通下单;1:拼团下单
     */
    private Byte orderMode;

    /**
     * 退款状态0:失败;1:成功
     */
    private Byte status;
}