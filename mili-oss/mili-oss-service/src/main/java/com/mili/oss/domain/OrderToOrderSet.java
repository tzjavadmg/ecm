package com.mili.oss.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单关系表
 */
@Getter
@Setter
public class OrderToOrderSet {
    /**
     * 
     */
    private Long id;

    /**
     * 集单ID
     */
    private Long orderSetDetailId;

    /**
     * 订单ID
     */
    private Long orderId;
}