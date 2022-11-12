package com.milisong.breakfast.pos.mq.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 集单打印的发送的MQ dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetPrintMsgDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 873808423729552842L;
	// 门店id
	private Long shopId;
}
