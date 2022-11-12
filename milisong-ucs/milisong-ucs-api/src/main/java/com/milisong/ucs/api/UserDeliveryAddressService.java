package com.milisong.ucs.api;


import com.farmland.core.api.ResponseResult;
import com.milisong.ucs.dto.Pagenation;
import com.milisong.ucs.dto.SendBreakCouponRequest;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.ucs.dto.UserDeliveryAddressRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/9/3 下午4:49
 * @description
 */
@FeignClient("milisong-ucs-service")
public interface UserDeliveryAddressService {

	@PostMapping(value = "/v1/UserDeliveryAddressService/saveDeliveryAddr")
    ResponseResult<Boolean> saveDeliveryAddr(@RequestBody UserDeliveryAddressDto userDeliveryAddr);
	
	@PostMapping(value = "/v1/UserDeliveryAddressService/updateDeliveryAddr")
    ResponseResult<Boolean> updateDeliveryAddr(@RequestBody UserDeliveryAddressDto userDeliveryAddr);

	@PostMapping(value = "/v1/UserDeliveryAddressService/saveOrUpdateTempDeliveryAddr")
	ResponseResult<Boolean> saveOrUpdateTempDeliveryAddr(@RequestBody UserDeliveryAddressDto userDeliveryAddr);

	@PostMapping(value = "/v1/UserDeliveryAddressService/queryDeliveryAddr")
    ResponseResult<UserDeliveryAddressDto> queryDeliveryAddr(@RequestBody UserDeliveryAddressRequest request);

	@PostMapping(value = "/v1/UserDeliveryAddressService/queryDeliveryById")
    ResponseResult<UserDeliveryAddressDto> queryDeliveryById(@RequestParam("addressId") Long addressId);

	@PostMapping(value = "/v1/UserDeliveryAddressService/queryDeliveryAddrByManyWhere")
    ResponseResult<UserDeliveryAddressDto> queryDeliveryAddrByManyWhere(@RequestParam("mobileNo") String mobileNo,
    		@RequestParam("deliveryOfficeBuildingId") Long deliveryOfficeBuildingId,@RequestParam("deliveryFloor") String deliveryFloor,
    		@RequestParam("deliveryCompany") String deliveryCompany);

	@PostMapping(value = "/v1/UserDeliveryAddressService/queryDeliveryAddrByMobileAndId")
    ResponseResult<UserDeliveryAddressDto> queryDeliveryAddrByMobileAndId(@RequestParam("id") Long id, @RequestParam("mobile") String mobile);


	@PostMapping(value = "/v1/UserService/queryUserByDevlieryAddr")
	ResponseResult<Pagenation<UserDeliveryAddressDto>> queryUserByDevlieryAddr(@RequestBody SendBreakCouponRequest sendBreakCouponRequest);

	@PostMapping(value = "/v1/UserService/queryDeliveryCompany")
	ResponseResult<List<UserDeliveryAddressDto>> queryDeliveryCompany(@RequestParam("companyName") String companyName,@RequestParam("businessLine") Byte businessLine);

	@PostMapping(value = "/v1/UserService/queryDeliveryCompanyNames")
	ResponseResult<List<UserDeliveryAddressDto>> queryDeliveryCompanyNames(@RequestParam("deliveryOfficeBuildingIds") List<Long> deliveryOfficeBuildingIdList);

}