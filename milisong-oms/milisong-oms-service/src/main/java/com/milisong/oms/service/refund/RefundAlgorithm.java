package com.milisong.oms.service.refund;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @ Description：退款策略
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 10:54
 */
public interface RefundAlgorithm {

    @Deprecated
    BigDecimal calc(RefundAlgorithmContext refundAlgorithmContext);

    RefundDto calculate(RefundAlgorithmContext refundAlgorithmContext);

    @Getter
    @Setter
    static public class RefundDto {
        BigDecimal refundAmount;
        Integer refundPoints;
    }

}
