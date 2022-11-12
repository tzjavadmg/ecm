package com.milisong.breakfast.scm.orderset.dto.param;

import java.io.Serializable;

import com.milisong.dms.dto.shunfeng.DeliveryOrderMqDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 修改集单状态的入参dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class UpdateOrderSetStatusParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6603851038003415997L;
	// 子集ID
	private Long detailSetId;
	// 操作类型  1打包 2交配送 3通知客户
	private Integer action;
	// 操作人
	private String updateBy;
	//子集单编号
	private String setNo;
	//打印类型
	private Integer printType;
	// 配送的DTO
	private DeliveryOrderMqDto sfDto;
}
