package com.milisong.breakfast.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.List;

import com.milisong.breakfast.scm.order.dto.result.OrderDetailResult;

import lombok.Getter;
import lombok.Setter;

/**
 * 通知时查询集单信息的返回dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class NotifyOrderSetQueryResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5836460777129605080L;
	// 子集单信息
	private OrderSetDetailDto orderSet;
	// 集单里的商品详细数据
	private List<OrderSetDetailGoodsDto> goodsList;
	
	//配送单号
	private String distributionNo;
	
	private String ordersetNo;
	
	// 打印类型 1=集单打印、2=顾客联打印
	private Integer printType;
	//原单明细
	private List<OrderDetailResult> listOrderDetails;
	
	private String coupletNo;
	
}
