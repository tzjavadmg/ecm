package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSetStatusQueryResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6580561028900445989L;

	// 订单编号
	private String orderNo;
	
	private List<OrderSetStatusQueryResultItem> detail;
}
