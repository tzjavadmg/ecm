package com.milisong.breakfast.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.List;

import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailGoodsDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 集单的MQ
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetProductionMsgByPrint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2960852626322938687L;
	// 集单信息
	private OrderSetDetailDto orderSet;
	// 集单里的商品信息
	private List<OrderSetDetailGoodsDto> goods;
}
