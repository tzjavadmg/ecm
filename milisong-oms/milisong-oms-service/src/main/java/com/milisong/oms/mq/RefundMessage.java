package com.milisong.oms.mq;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/11 11:26
 */
@Getter
@Setter
public class RefundMessage {

    private Long deliveryId;

    private String deliveryNo;

    private Date deliveryDate;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 退还积分
     */
    private Integer totalRefundPoints;

    private String shopCode;

    private Byte orderType;

    private List<RefundGoodsMessage> refundGoods;

}
