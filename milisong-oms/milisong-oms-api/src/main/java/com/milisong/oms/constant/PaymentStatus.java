package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 19:17
 */
@Getter
@AllArgsConstructor
public enum PaymentStatus {

    /**
     * 未支付
     */
    FAILED((byte) 0),

    /**
     * 支付成功
     */
    SUCCEED((byte) 1);


    private final byte value;
}
