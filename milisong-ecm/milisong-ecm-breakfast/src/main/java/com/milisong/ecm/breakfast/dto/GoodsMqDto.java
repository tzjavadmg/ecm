package com.milisong.ecm.breakfast.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsMqDto {
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
     * 商品类型 1单品 2组合商品
     */
    private Byte type;

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
     * 辣度 0不辣 1微辣 2中辣 3重辣 4变态辣
     */
    private Byte spicy;

    /**
     * 保质期(天)
     */
    private Byte shelfLife;

    /**
     * 口味
     */
    private String taste;

    /**
     * 排序号(权重)
     */
    private Byte weight;

    /**
     * 是否为组合套餐（1是、0否）
     */
    private Boolean isCombo;

    /**
     * 商品状态 1使用中 2已停用
     */
    private Byte status;

    /**
     * 是否新品
     */
    private Boolean isNewGoods;
    
    /**
     * 组合商品  子商品信息
     */
   	private List<GoodsCombinationRefDto> listGoodsCombinationRefMqDto;
}
