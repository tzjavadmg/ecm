package com.milisong.ecm.breakfast.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyBucketDto extends CompanyDto{
	
	public List<CompanyMealAddressDto> mealAddressList;
	//早餐取餐时间
	public List<CompanyMealTimeDto> mealTimeList;
	//午餐取餐时间
	public List<CompanyMealTimeDto> lunchMealTimeList;

}
