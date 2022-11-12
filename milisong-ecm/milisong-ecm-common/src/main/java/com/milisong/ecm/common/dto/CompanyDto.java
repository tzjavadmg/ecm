package com.milisong.ecm.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {
	
	//楼宇id
	private Long buildingId;
	
	//消息类型，1:楼层 2:公司名
	private Integer type;
	
	//修改前
	private String reviseFront;
	
	//修改后
	private String reviseAfter;

}
