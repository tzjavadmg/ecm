package com.mili.oss.dto.result;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author benny
 */
@Getter
@Setter
public class ShopGoodsCount{

    /**
     * 可售日期
     */
    private Date deliveryDate;

    /**
     * 商品code
     */
    private String goodsCode;

    /**
     * 商品数量
     */
    private Integer goodsCount;
}
