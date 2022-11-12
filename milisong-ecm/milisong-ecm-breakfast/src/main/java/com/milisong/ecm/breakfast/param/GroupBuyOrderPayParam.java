package com.milisong.ecm.breakfast.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/21 16:20
 */
@Getter
@Setter
public class GroupBuyOrderPayParam {

    private String openId;

    private String orderIp;

    private String shopCode;

    private String realName;

    private String mobileNo;

    private Short sex;

    private String deliveryCompany;

    private String deliveryBuildingName;

    private String deliveryFloor;
    /**
     * 公司ID
     */
    private Long deliveryCompanyId;

    /**
     * 用户地址ID
     */
    private Long deliveryAddressId;
    /**
     * 取餐点
     */
    private Long takeMealsAddrId;

    private String takeMealsAddrName;
    /**
     * 配送日期
     */
    private String deliveryDate;

    /**
     * 聚餐时间开始
     */
    private String takeMealsStartTime;
    /**
     * 聚餐时间结束
     */
    private String takeMealsEndTime;

    private String formId;

    private String goodsCode;
    /**
     * 拼团活动ID
     */
    private Long activityGroupBuyId;
    /**
     * 团ID(参与已开团)
     */
    private Long userGroupBuyId;
}
