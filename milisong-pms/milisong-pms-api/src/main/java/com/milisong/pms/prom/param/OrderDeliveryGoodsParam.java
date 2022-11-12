package com.milisong.pms.prom.param;

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
public class OrderDeliveryGoodsParam {

    private Long id;

    private String goodsCode;

    private String goodsName;

    private Integer goodsCount;

    private BigDecimal goodsOriginalPrice;

    private BigDecimal goodsActualPrice;

    private BigDecimal packageOriginalPrice;

    private BigDecimal packageActualPrice;

    private String goodsImgUrl;

    @Override
    public String toString() {
        return "OrderDeliveryGoodsParam{" +
                "id=" + id +
                ", goodsCode='" + goodsCode + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsCount=" + goodsCount +
                ", goodsOriginalPrice=" + goodsOriginalPrice +
                ", goodsActualPrice=" + goodsActualPrice +
                ", packageOriginalPrice=" + packageOriginalPrice +
                ", packageActualPrice=" + packageActualPrice +
                ", goodsImgUrl='" + goodsImgUrl + '\'' +
                '}';
    }
}
