package com.milisong.scm.printer.request.mq;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 集单的MQ
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetProductionMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2960852626322938687L;
	// 集单信息
	private OrderSetDetailDto orderSet;
	// 集单里的商品信息
	private List<OrderSetDetailGoodsDto> goods;
	
	private String coupletNo;
	
}
