package com.milisong.breakfast.scm.order.dto.param;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单明细表-组合明细 查询条件
 */
@Getter
@Setter
public class OrderDetailComboParam implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7796711118069378252L;


    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 组合商品code
     */
    private String comboGoodsCode;

}