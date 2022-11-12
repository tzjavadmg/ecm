package com.milisong.breakfast.pos.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 门店信息dto
 * @author youxia 2018年9月2日
 */
@Data
public class ShopDto implements Serializable {
	
	private static final long serialVersionUID = -1563375216090449268L;
	
	public Long shopId; // 门店id
	
	private String shopCode; // 门店编码
	
	private String shopName; // 门店名称
	
	private Byte status; // 状态
	
	private String address; // 门店地址

}
