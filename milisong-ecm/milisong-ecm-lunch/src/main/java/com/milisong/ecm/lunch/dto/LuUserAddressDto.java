package com.milisong.ecm.lunch.dto;

import com.milisong.ucs.dto.UserDeliveryAddressDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LuUserAddressDto extends UserDeliveryAddressDto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public List<CompanyMealAddressDto> mealAddressList;

	public List<CompanyMealTimeDto> deliveryTimeList;

}
