package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配送状态
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 16:48
 */
@Getter
@AllArgsConstructor
public enum GoodsType {
    /**
     * 主餐
     */
    MAIN_MEAL((byte) 0),

    /**
     * 配菜
     */
    PICKLES((byte) 1);

    private final byte value;
}
