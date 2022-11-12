package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @ Description：支付结果
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/28 10:09
 */
@Getter
@Setter
public class PaymentResultParam {

    Long orderId;
    Boolean isSuccess;
    String errorCode;
    String errCodeDes;
}
