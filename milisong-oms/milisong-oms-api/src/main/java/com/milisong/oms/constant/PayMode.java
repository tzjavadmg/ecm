package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/7 10:06
 */
@Getter
@AllArgsConstructor
public enum PayMode {
    /**
     * 微信代扣
     */
    WECHAT_WITHHOLD((byte) 0),

    /**
     * 微信支付
     */
    WECHAT_PAY((byte) 1),

    /**
     * 支付宝代扣
     */
    ALIPAY_WITHHOLD((byte) 2),

    /**
     * 支付宝支付
     */
    ALIPAY_PAY((byte) 3);

    private final byte value;
}
