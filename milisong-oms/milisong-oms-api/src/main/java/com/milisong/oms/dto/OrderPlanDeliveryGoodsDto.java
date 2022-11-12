package com.milisong.oms.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlanDeliveryGoodsDto {
    private Long id;

    //商品code
    private String goodsCode;
    
    //商品图
    private String picture;
}
