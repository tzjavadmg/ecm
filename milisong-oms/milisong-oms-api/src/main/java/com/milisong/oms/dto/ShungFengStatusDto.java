package com.milisong.oms.dto;



import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShungFengStatusDto {
	
	//顺风状态0:已派单; 1: 已接单;2: 已到店;3: 已提货;4: 已送达
	private Byte status;
	
	//状态值名称
	private String statusName;
	
	//完成时间(mm-dd hh:mm)
	private String completeTime;
	

}
