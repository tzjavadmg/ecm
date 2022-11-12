package com.milisong.breakfast.scm.goods.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月4日---下午9:35:20
*
*/
@Setter
@Getter
public class GoodsMqDto {

	private Long id;
	private String code;
	private String name;
	private String picture;//图片
    private String describe;//描述
    private Integer weight;//权重
    private BigDecimal advisePrice;//原价
    private BigDecimal preferentialPrice;//优惠价
    private String bigPicture; //大图
    private String details;//详细描述
    private String taste;//口味
    private Integer spicy;//辣度 0不辣 1微辣 2中辣 3重辣 4变态辣
}
