package com.mili.oss.dto.begin;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAmount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2048847471628469270L;

	private Long userId;

    private BigDecimal totalAmount;
}
