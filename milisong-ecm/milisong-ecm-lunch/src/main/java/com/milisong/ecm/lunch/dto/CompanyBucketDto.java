package com.milisong.ecm.lunch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyBucketDto extends CompanyDto{
	
	public List<CompanyMealAddressDto> mealAddressList;
	//午餐取餐时间
	public List<CompanyMealTimeDto> lunchMealTimeList;

}
