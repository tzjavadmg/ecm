package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 10:41
 */
@Getter
@AllArgsConstructor
public enum SystemModule {

    /**
     * 系统标识
     */
    SYS_OMS("oms"),
    /**
     * 虚拟订单模块
     */
    MOD_VIRTUAL_ORDER("virtual_order"),
    /**
     * 虚拟订单模块
     */
    MOD_GROUP_BUY_ORDER("group_buy_order"),
    /**
     * 订单模块
     */
    MOD_ORDER("order");

    private String value;

}
