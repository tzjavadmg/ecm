package com.milisong.breakfast.scm.goods.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月11日---下午6:57:52
*
*/
@Getter
@Setter
public class GoodsCombinationDto {

	/**
     * 主键
     */
    private Long id;

    /**
     * 商品code/sku
     */
    private String code;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 类目code
     */
    private String categoryCode;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 商品详情
     */
    private String describe;

    /**
     * 建议零售价
     */
    private BigDecimal advisePrice;

    /**
     * 实际售价
     */
    private BigDecimal preferentialPrice;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 小图片
     */
    private String picture;

    /**
     * 大图片
     */
    private String bigPicture;
    
    /**
     * 小图片
     */
    private String pictureShow;

    /**
     * 大图片
     */
    private String bigPictureShow;



    /**
     * 口味
     */
    private String taste;

    /**
     * 排序号(权重)
     */
    private Byte weight;
    
    /**
     * 商品数量
     */
    private Integer goodsCount;

}
