package com.milisong.dms.dto.orderset;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 集单的MQ
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetProductionMqMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2960852626322938687L;
	// 集单信息
	private OrderSetDetailMqDto orderSet;
	// 集单里的商品信息
	private List<OrderSetDetailGoodsMqDto> goods;
}
