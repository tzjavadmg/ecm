package com.milisong.oms.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUserDetailDto {
	
    private Long id;

    //商品code
    private String goodsCode;
    
    //名称
    private String goodsName;

    //图片
    private String picture;
    
    //数量
    private Integer goodsCount;
    
    //价格
    private BigDecimal goodsPrice;
    
    //商品折扣价格
    private BigDecimal goodsDiscountPrice;
    
}
