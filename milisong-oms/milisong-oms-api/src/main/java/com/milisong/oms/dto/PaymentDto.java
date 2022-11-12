package com.milisong.oms.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/3 15:33
 */
@Getter
@Setter
public class PaymentDto {

    private boolean isConfirmPre;

    private String openId;

    private long orderId;

    private String contractNo;

    private String sessionKey;

    private String orderIp;

    private String realName;

    private String mobileNo;

    private Long deliveryOfficeBuildingId;

    private Long deliveryCompanyId;

    private Short sex;

    private String deliveryFloor;

    private String deliveryRoom;

    private String deliveryCompany;

    private String deliveryAddress;
}
