package com.milisong.dms.dto.httpdto;

import com.milisong.dms.dto.orderset.OrderSetDetailDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
	
	private List<DistributionOrdersetResult> distributionOrdersetResultList;
	//配送单号
	private String distributionNo;
	
	private String ordersetNo;
	
	// 打印类型 1=集单打印、2=顾客联打印
	private Integer printType;
	//原单明细
	private List<OrderDetailResult> listOrderDetails;
	
	private String coupletNo;
	
}
