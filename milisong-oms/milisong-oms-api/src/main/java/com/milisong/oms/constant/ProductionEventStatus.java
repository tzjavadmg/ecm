package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/27 10:57
 */
@Getter
@AllArgsConstructor
public enum ProductionEventStatus {

    /**
     * 已发送
     */
    SEND((byte) 0),

    /**
     * 发送成功
     */
    BROKER_RECEIVED((byte) 1),

    /**
     * 发送失败
     */
    BROKER_UNRECEIVED((byte) 2);

    private final Byte value;
}
