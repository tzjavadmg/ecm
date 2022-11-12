package com.milisong.ecm.breakfast.dto;

import com.milisong.ucs.dto.UserDeliveryAddressDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * LBS页面的信息
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class LbsAddressResultDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8227170851773112774L;
	// 当前定位的地址
	private String nowAddress;
	// 最近的配送地址信息
	private UserDeliveryAddressDto nearbyDeliveryAddress;
	// 最近配送的楼宇Id
	private Long nearbyBuildingId;
	// 最近配送的楼宇信息
	private String nearbyBuildingName;
	// 附近大楼
//	BuildingLbsDto

	private List<CompanyDto> nearbyBuildings;

}
