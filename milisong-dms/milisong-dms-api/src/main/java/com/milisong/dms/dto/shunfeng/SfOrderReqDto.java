package com.milisong.dms.dto.shunfeng;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SfOrderReqDto implements Serializable {

    private static final long serialVersionUID = -5620337644570551071L;
    private String devId;

    private Integer version;
    /** sf门店id */
    private String shopId;
    /** 门店类型 1、顺丰店铺ID ；2、接入方店铺ID */
    private Byte shopType;
    /** 子集单id  */
    private String shopOrderId;
    /** 坐标系 1、百度坐标，2、高德坐标 */
    private Byte lbsType;
    /** 订单类型 1、美团；2、饿了么；3、百度；4、口碑；直接写米立送  */
    private String orderSource;
    /** 取单号 */
    private String orderSequence;
    /**  支付方式 1、在线支付 0、货到付款 默认1 */
    private Byte payType;
    /**  下单时间 秒级 */
    private Long orderTime;
    /**  推单时间 秒级 */
    private Long pushTime;
    /** 用户期待送达时间 */
    private Long expectTime;
    /**  是否是预约单 0、非预约单；1、预约单 */
    private Byte isAppoint;
    /**  备注 */
    private String remark;
    /**  收货人信息 */
    private SfOrderReceiveDto receive;
    /**  订单信息 */
    private SfOrderDetail orderDetail;
    /** 公司取餐点信息 */
    private List<SfOrderCompanyInfoDto> companyDtoList;
    private String detailId;
}
