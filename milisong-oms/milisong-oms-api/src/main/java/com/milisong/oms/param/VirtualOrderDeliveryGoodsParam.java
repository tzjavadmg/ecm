package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/3 13:55
 */
@Getter
@Setter
public class VirtualOrderDeliveryGoodsParam {

    private String goodsCode;

    private String goodsName;

    private BigDecimal goodsPrice;

    private Integer goodsCount;

    private String goodsImgUrl;

    private BigDecimal goodsOriginalPrice;

    private BigDecimal goodsActualPrice;

    private BigDecimal packageOriginalPrice;

    private BigDecimal packageActualPrice;

    /**
     * 是否组合商品
     */
    private Boolean isCombo;
    /**
     * 组合商品ID
     */
    private Long comboGoodsId;

    /**
     * 组合商品编码
     */
    private String comboGoodsCode;
    /**
     * 0:主餐、1:小菜
     */
    private Byte type;
    /**
     * 组合商品明细
     */
    private List<VirtualOrderDeliveryGoodsParam> deliveryGoodsParamList;
}
