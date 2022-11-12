package com.milisong.pms.prom.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 15:02
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class OrderDeliveryParam {

    private Long id;

    private Long orderId;

    private String deliveryNo;

    private String shopCode;

    private String mobileNo;
    /**
     * 总商品数量
     */
    private Integer totalGoodsCount;
    /**
     * 预订日期
     */
    private String deliveryDate;
    /**
     * 今天，明天,周几
     */
    private String dayDesc;

    private BigDecimal deliveryOriginalPrice;

    private BigDecimal deliveryActualPrice;

    private BigDecimal totalPackageOriginalAmount;

    private BigDecimal totalPackageActualAmount;

    private BigDecimal totalGoodsOriginalAmount;

    private BigDecimal totalGoodsActualAmount;

    private BigDecimal shareRedPacketAmount;

    private BigDecimal totalAmount;

    private Byte status;

    private String statusName;

    private Boolean refundable;

    private Long deliveryTimezoneId;

    @JsonFormat(timezone = "GMT+8",pattern = "HH:mm")
    private Date deliveryTimezoneCutoffTime;
    @JsonFormat(timezone = "GMT+8",pattern = "HH:mm")
    private Date deliveryTimezoneFrom;
    @JsonFormat(timezone = "GMT+8",pattern = "HH:mm")
    private Date deliveryTimezoneTo;
    /**
     * 配送单商品明细
     */
    private List<OrderDeliveryGoodsParam> deliveryGoods;


}
