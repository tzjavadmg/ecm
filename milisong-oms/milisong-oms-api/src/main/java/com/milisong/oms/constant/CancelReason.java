package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/12 14:35
 */
@Getter
@AllArgsConstructor
public enum CancelReason {

    /**
     *
     */
    UNPAID_EXPIRED("支付超时系统自动取消");

    private final String value;
}
