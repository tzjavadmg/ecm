package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/22 10:41
 */
@AllArgsConstructor
@Getter
public enum ConfigItem {

    /**
     * 未支付订单，过期取消时间，单位：分钟
     */
    UN_PAY_EXPIRED_TIME("unPayExpiredTime");

    private String value;

}
