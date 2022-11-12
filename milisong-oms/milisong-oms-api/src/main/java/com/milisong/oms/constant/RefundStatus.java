package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/12 11:44
 */
@Getter
@AllArgsConstructor
public enum RefundStatus {

    /**
     * 退款失败
     */
    FAILED((byte) 0),

    /**
     * 退款成功
     */
    SUCCEED((byte) 1);


    private final byte value;
}
