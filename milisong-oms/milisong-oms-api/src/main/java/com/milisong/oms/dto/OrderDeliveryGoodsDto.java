package com.milisong.oms.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 15:02
 */
@Getter
@Setter
public class OrderDeliveryGoodsDto {

    private Long id;

    private String goodsCode;

    private String goodsName;

    private Integer goodsCount;

    private BigDecimal goodsOriginalPrice;

    private BigDecimal goodsActualPrice;

    private BigDecimal packageOriginalPrice;

    private BigDecimal packageActualPrice;

    private String goodsImgUrl;
    /**
     * 组合商品编码
     */
    private String comboGoodsCode;
    /**
     * 是否组合商品
     */
    private Boolean isCombo;
    /**
     * 0:主餐、1:小菜
     */
    private Byte type;
}
