package com.milisong.oms.param;

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
 * @date 2018/10/3 13:53
 */
@Getter
@Setter
public class VirtualOrderDeliveryParam {

    /**
     * 配送时间
     */
    private Date deliveryDate;
    /**
     * 配送费原价
     */
    private BigDecimal deliveryOriginalPrice;
    /**
     * 配送费实价
     */
    private BigDecimal deliveryActualPrice;
    /**
     * 预订哪几种饭
     */
    private List<VirtualOrderDeliveryGoodsParam> deliveryGoods;
}
