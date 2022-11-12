package com.milisong.oms.dto;

import com.milisong.oms.constant.GoodsType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/3 15:24
 */
@Getter
@Setter
public class VirtualOrderDeliveryDto {

    private Long id;
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

    private BigDecimal totalAmount;

    private Long timezoneId;
    /**
     * 配送开始时间
     */
    private String deliveryStartTime;

    /**
     * 配送结束时间
     */
    private String deliveryEndTime;
    /**
     * 配送单商品明细
     */
    private List<VirtualOrderDeliveryGoodsDto> deliveryGoods;


}
