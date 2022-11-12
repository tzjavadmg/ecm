package com.milisong.breakfast.scm.goods.param;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月11日---下午5:42:35
*
*/
@Getter
@Setter
public class GoodsCombinationParam {

	/**
     * 单品商品code
     */
    private String singleCode;
    
    /**
     * 单品数量
     */
    private Integer goodsCount;
}
