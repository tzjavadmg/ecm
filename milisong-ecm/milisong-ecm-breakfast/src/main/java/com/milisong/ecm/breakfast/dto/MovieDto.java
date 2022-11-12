package com.milisong.ecm.breakfast.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDto {
	
	/**
	 * 档期日期
	 */
	@JsonFormat(timezone = "GMT+8",pattern = "yyy-MM-dd")
	private Date scheduleDate;
	
    /**
     * W多少周
     */
    private Integer weekOfYear;
    
    /**
     * 是可可售 1可售 0不可售
     */
    private Boolean status;
	
	
	/**
	 * 周几
	 */
	private String day;
	
	/**
	 * 日期毫秒
	 */
	private Long dateMesc;




}
