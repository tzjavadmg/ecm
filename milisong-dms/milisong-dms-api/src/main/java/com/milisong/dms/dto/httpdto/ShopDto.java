package com.milisong.dms.dto.httpdto;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店信息dto
 * @author youxia 2018年9月2日
 */
@Data
public class ShopDto implements Serializable {
	
	private static final long serialVersionUID = -1563375216090449268L;

	private Long id;

	private String code;

	private String name;

	//门店状态:1营业中 2停业
	private Byte status;

	private String address;

	private Boolean isDelete;

}
