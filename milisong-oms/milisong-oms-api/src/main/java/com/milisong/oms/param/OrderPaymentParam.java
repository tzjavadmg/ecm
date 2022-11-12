package com.milisong.oms.param;

import com.milisong.oms.dto.TimezoneDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 19:11
 */
@Getter
@Setter
public class OrderPaymentParam {

    private String openId;

    private Date orderDate;

    private String companyWorkingTime;

    private String deliveryTimezoneFrom;

    private String deliveryTimezoneTo;

    private String shopCode;

    private byte orderType;

    private Byte orderSource;

    private Long orderId;

    private Long redPacketId;

    private Long couponId;

    /**
     * 抵扣使用积分
     */
    private Integer deductionPoints;

    private String orderIp;

    private String realName;

    private String mobileNo;

    private Short sex;

    private Short notifyType;

    private Long deliveryBuildingId;

    private String deliveryBuildingName;

    private String deliveryFloor;

    private String deliveryCompany;

    private Long deliveryAddressId;

    private String cutoffTime;
    /**
     * 取餐点
     */
    private Long takeMealsAddrId;

    private String takeMealsAddrName;

    private String formId;

    private List<DeliveryTimezoneParam> deliveryTimezones;

    private List<TimezoneDto> timezoneDtos;

}
