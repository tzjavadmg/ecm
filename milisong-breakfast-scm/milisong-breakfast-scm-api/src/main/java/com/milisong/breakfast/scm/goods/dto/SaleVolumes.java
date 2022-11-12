package com.milisong.breakfast.scm.goods.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月24日---下午7:44:05
*
*/
@Data
public class SaleVolumes {

	@JsonFormat(pattern="MM-dd",timezone = "GMT+8")
	private Date saleDate;
	
	private int volumes;
}
