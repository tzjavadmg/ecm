package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/6 16:04
 */
@Getter
@AllArgsConstructor
public enum CommonStatus {

    /**
     * 成功
     */
    SUCCESS((short)1),

    /**
     * 失败
     */
    FAIL((short)0);


    private final short value;
}
