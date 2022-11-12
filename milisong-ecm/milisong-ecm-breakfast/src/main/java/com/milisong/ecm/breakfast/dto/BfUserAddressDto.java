package com.milisong.ecm.breakfast.dto;

import java.util.List;

import com.milisong.ucs.dto.UserDeliveryAddressDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BfUserAddressDto extends UserDeliveryAddressDto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public List<CompanyMealAddressDto> mealAddressList;

	public List<CompanyMealTimeDto> deliveryTimeList;

}
