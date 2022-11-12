package com.mili.oss.dto.result;

import java.io.Serializable;

import com.mili.oss.constant.OrderDeliveryStatus;
import com.mili.oss.constant.OrderSetStatusEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * 集单状态dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetDetailStatusDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7974048577086086797L;
	/**
	 * 集单状态
	 * {@link OrderSetStatusEnum}
	 */
	private Byte status;
	/**
	 * 物流状态
	 * {@link OrderDeliveryStatus}
	 */
	private Byte distributionStatus;
}
