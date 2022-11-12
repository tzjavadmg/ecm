package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 9:38
 */
@Getter
@AllArgsConstructor
public enum TimezoneCapacitySouce {

    /**
     * 创建订单
     */
    CREATE_ORDER((byte) 0),
    /**
     * 取消订单
     */
    CANCEL_ORDER((byte) 1),
    /**
     * 订单退款
     */
    REFUND_ORDER((byte) 2),
    /**
     * 设置产能
     */
    SET_CAPACITY((byte) 3);

    private final byte value;
}
