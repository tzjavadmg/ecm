package com.milisong.breakfast.scm.goods.dto;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月13日---下午2:11:40
*
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

}
