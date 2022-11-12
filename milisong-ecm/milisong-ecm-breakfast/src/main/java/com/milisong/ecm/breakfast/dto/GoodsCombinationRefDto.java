package com.milisong.ecm.breakfast.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author created by benny
 * @date 2018年12月11日---下午3:12:10
 */
@Getter
@Setter
public class GoodsCombinationRefDto {

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
     * 商品名称
     */
    private String goodsName;

    private BigDecimal advisePrice;

}
