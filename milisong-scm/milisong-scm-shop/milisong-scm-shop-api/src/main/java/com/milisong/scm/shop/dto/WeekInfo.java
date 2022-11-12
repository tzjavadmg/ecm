package com.milisong.scm.shop.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月3日---下午4:44:09
*
*/
@Getter
@Setter
public class WeekInfo {

	@JsonFormat(pattern="MM-dd",timezone = "GMT+8")
	private Date startDate;
	
	@JsonFormat(pattern="MM-dd",timezone = "GMT+8")
	private Date endDate;
	
	private String week;
	
	private String year;
	
}
