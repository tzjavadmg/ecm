package com.milisong.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ Description：返回门店商品的销量和剩余库存
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/5 13:48
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopOnSaleGoodsDto {

    private String goodsCode;

    private Integer monthlySales;

    private Integer stock;
}
