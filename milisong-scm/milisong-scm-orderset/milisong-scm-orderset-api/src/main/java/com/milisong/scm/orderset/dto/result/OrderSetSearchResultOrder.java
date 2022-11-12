package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单集单中的订单信息
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetSearchResultOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8565008145868395478L;
	// 订单号
	private String orderNo;
	// 客户id
    private Long userId;
    // 客户名字
    private String userName;
    // 客户手机号
    private String userPhone;
    // 商品编号
    private String goodsCode;
    // 商品名称
    private String goodsName;
    // 商品数量
    private Integer goodsNumber;
}
