package com.milisong.oms.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/3 13:51
 */
@Getter
@Setter
public class VirtualOrderParam {

    private boolean isConfirm;

    private Date orderDate;

    private Long userId;

    private byte orderType;

    private String openId;

    private String shopCode;

    private Long buildingId;

    private List<VirtualOrderDeliveryParam> deliveries;

}
