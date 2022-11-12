package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/3 9:25
 */
@Getter
@AllArgsConstructor
public enum OrderUserSource {

    /**
     * 米立早餐
     */
    WECHAT_MINI_BF((byte) 0),

    /**
     * 米立送
     */
    WECHAT_MINI_LC((byte) 1);

    private final byte value;
}
