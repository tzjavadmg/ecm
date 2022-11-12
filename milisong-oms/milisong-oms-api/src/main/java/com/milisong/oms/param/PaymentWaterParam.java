package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/12 11:43
 */
@Getter
@Setter
public class PaymentWaterParam {
    Long orderId;
    String errorCode;
    String errorDescription;
}
