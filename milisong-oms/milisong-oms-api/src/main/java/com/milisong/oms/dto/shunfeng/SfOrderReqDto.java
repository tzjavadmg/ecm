package com.milisong.oms.dto.shunfeng;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SfOrderReqDto{
    /** sf门店id */
    private String shopId;
    /** 门店类型 1、顺丰店铺ID ；2、接入方店铺ID */
    private Byte shopType;
    /** 订单id  */
    private String shopOrderId;
    /** 订单类型 1、美团；2、饿了么；3、百度；4、口碑；直接写米立送  */
    private String orderSource;
    /**  取货序号 */
    private String orderSequence;
    /**  坐标系 1、百度坐标，2、高德坐标 默认2 */
    private Byte lbsType;
    /**  支付方式 1、在线支付 0、货到付款 默认1 */
    private Byte payType;
    /**  下单时间 秒级 */
    private Long orderTime;
    /**  推单时间 秒级 */
    private Long pushTime;
    /**  是否是预约单 0、非预约单；1、预约单 */
    private Byte isAppoint;
    /**  备注 */
    private String remark;
    /**  收货人信息 */
    private SfOrderReceiveDto receiveDto;
    /**  订单信息 */
    private SfOrderDetail orderDetail;

}
