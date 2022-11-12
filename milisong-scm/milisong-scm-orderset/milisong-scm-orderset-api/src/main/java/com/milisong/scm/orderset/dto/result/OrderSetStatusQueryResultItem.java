package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSetStatusQueryResultItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2420763033788739810L;
	// 主集单编号(楼宇)
	private String orderSetNo;
	// 子集单编号(办公室)
	private String orderSetDetailNo;
	// 子集单中的商品信息
	private List<OrderSetStatusQueryResultItemGoods> goods;
	// 子集单状态  1待打包 2已打包 3配送中 4已通知
	private Byte status;
}
