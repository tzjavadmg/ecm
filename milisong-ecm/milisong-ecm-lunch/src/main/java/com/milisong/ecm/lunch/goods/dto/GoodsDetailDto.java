package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GoodsDetailDto {
    private String name;//名称
    private String bigPicture; //大图
    private Integer monthSale;//月销数量
    private String details;//详细描述
    private String taste;//口味
    private Integer spicy;//辣度 0不辣 1微辣 2中辣 3重辣 4变态辣
    private BigDecimal advisePrice;//原价
    private BigDecimal preferentialPrice;//优惠价
    private Integer stock;//库存

}
