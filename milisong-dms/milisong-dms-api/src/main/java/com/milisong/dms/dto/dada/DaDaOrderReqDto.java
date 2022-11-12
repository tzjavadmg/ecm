package com.milisong.dms.dto.dada;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhaozhonghui
 * @date 2018-11-23
 */
@Data
public class DaDaOrderReqDto implements Serializable {
    private static final long serialVersionUID = -8072960453666395824L;
    /** 门店编号 */
    private String shopNo;
    /** 第三方订单ID */
    private String originId;
    /** 城市编码 */
    private String cityCode;
    /** 订单金额 */
    private BigDecimal cargoPrice;
    /** 是否需要垫付 */
    private Byte isPrepay;
    /** 收货人名称 */
    private String receiverName;
    /** 收货人地址 */
    private String receiverAddress;
    /** 收货人纬度(高德) */
    private Double receiverLat;
    /** 收货人经度(高德) */
    private Double receiverLng;
    /** 回调url */
    private String callback;
    /** 收货人电话 */
    private String receiverPhone;
    /** 备注 */
    private String info;
    /** 订单类型  */
    private Byte cargoType;
}
