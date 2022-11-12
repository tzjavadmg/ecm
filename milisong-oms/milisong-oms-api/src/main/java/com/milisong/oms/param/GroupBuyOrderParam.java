package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 15:21
 */
@Getter
@Setter
public class GroupBuyOrderParam {

    private String nickName;

    private String headPortraitUrl;

    private Long userId;

    private byte orderType;

    private Byte orderSource;

    private String openId;

    private String shopCode;

    private Date orderDate;

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
    /**
     * 取餐点
     */
    private Long takeMealsAddrId;

    private String takeMealsAddrName;

    private String formId;
    /**
     * 拼团实例ID
     */
    private Long groupBuyId;

    /**
     * 团购活动ID
     */
    private Long groupBuyActivityId;

    private List<GroupBuyOrderDeliveryParam> deliveries;
}
