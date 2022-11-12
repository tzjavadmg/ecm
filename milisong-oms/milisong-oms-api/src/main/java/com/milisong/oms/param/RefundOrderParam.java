package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/9 18:28
 */
@Getter
@Setter
public class RefundOrderParam {

    private Long orderId;

    private Set<Long> deliveryIds;

    private String cancelReason;
}
