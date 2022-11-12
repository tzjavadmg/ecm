package com.milisong.ucs.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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

    /**
     * 配送时间id
     */
    private Long deliveryTimeId;

    /**
     * 配送开始时间
     */
    @JSONField(format = "HH:mm")
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeBegin;

    /**
     * 配送结束时间
     */
    @JSONField(format = "HH:mm")
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeEnd;

}