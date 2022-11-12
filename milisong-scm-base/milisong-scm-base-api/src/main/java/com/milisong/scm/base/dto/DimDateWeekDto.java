package com.milisong.scm.base.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月8日---下午2:02:51
*
*/
@Getter
@Setter
public class DimDateWeekDto {

	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date nowDate;
}
