package com.milisong.oms.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @ Description：查询门店某天商品的销量和剩余库存
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/5 13:42
 */
@Getter
@Setter
public class ShopOnSaleGoodsParam {

    private String shopCode;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date saleDate;

    private List<OnSaleGoodsParam> goodsCodes;

    @Getter
    @Setter
    public static class OnSaleGoodsParam {

        private Boolean isCombo;

        private String goodsCode;

        private List<GoodsComboInfo> comboInfos;
    }

    @Getter
    @Setter
    public static class GoodsComboInfo{

        private String goodsCode;

        private Integer goodsCount;
    }

}
