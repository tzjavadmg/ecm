package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/11 15:32
 */
@Getter
@Setter
public class CancelOrderParam {

    private Long orderId;

    private String cancelReason;
}
