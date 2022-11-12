package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSetStatusQueryResultItemGoods implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2420763033788739810L;
	
	private Long id;

    private String detailSetNo;

    private String orderNo;

    private Long userId;

    private String userName;

    private String userPhone;

    private String goodsCode;

    private String goodsName;

    private Integer goodsNumber;
}
