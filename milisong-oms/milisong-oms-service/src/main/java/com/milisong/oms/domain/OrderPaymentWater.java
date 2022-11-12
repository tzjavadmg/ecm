package com.milisong.oms.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付流水表.用于记录微信支付流水
 * 1.调用微信预下单(唤起微信支付密码输入界面)接口，如果失败记录失败流水，如果成功转到步骤2.
 * 2.微信支付回调(输入密码成功)时，根据返回结果，记录成功或失败流水
 */
@Getter
@Setter
public class OrderPaymentWater {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

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
     * 支付状态0:失败;1:成功
     */
    private Byte status;
}