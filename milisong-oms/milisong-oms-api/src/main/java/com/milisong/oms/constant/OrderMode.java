package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 20:32
 */
@Getter
@AllArgsConstructor
public enum OrderMode {

    /**
     * 普通下单
     */
    COMMON((byte) 0),

    /**
     * 拼团下单
     */
    GROUP_BUY((byte) 1);

    private final byte value;
}
