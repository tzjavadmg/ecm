package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeliveryAddressDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -7018962581429147859L;

    private Long id;

    private Long userId;

    private String realName;

    private String mobileNo;

    private Integer sex;

    private Long deliveryOfficeBuildingId;

    private Long deliveryCompanyId;

    private String deliveryAddress;

    private String deliveryFloor;

    private String deliveryRoom;
    
    private String deliveryCompany;
    
    private Integer notifyType;
    
    //午餐点id
    private Long takeMealsAddrId;
    
    //取餐点地址
    private String takeMealsAddrName;

}