package com.milisong.breakfast.scm.goods.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月11日---下午3:12:10
*
*/
@Getter
@Setter
public class GoodsCombinationRefMqDto {

	 /**
     * 
     */
    private Long id;

    /**
     * 组合商品code
     */
    private String combinationCode;

    /**
     * 单品商品code
     */
    private String singleCode;
    
    /**
     * 单品数量
     */
    private Integer goodsCount;
    /**
     * 商品name
     */
    private String goodsName;
    
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
     * 辣度 0不辣 1微辣 2中辣 3重辣 4变态辣
     */
    private Byte spicy;


    /**
     * 排序号(权重)
     */
    private Byte weight;

    /**
     * 商品状态 2使用中 5已停用
     */
    private Byte status;
    
	
    /**
     * 是否为组合套餐（1是、0否）
     */
    private Boolean isCombo;
    
}
