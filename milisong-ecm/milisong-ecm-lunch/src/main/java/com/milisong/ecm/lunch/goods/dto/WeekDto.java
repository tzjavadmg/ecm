package com.milisong.ecm.lunch.goods.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WeekDto {
	
	private String week;
	
	private Long dateConver;
	
	private int status;
	@JsonFormat(timezone = "GMT+8",pattern = "yyy-MM-dd")
	private Date date;
	

}
