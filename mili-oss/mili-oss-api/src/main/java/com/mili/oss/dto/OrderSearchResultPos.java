package com.mili.oss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhaozhonghui
 * @date 2018-11-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderSearchResultPos implements Serializable {
    private static final long serialVersionUID = -8521710995401914958L;
    /** 集单id */
    private Long id;
    /** 配送单 */
    private String description;
    /** 顾客联编号 */
    private String coupletNo;
    /** 下单日期 */
    private String deliveryDate;
    /** 期望到达时间 */
    private String expectArriveTime;
    /** 实际到达时间 */
    private String arriveTime;
    /** 用户信息 用户名_手机 */
    private String userInfo;
    /** 公司 */
    private String company;
    /** 取餐点 */
    private String mealAddress;
    /** 商品名称 */
    private List<OrderGoodsResult> goodsResultList;
    /** 备注 */
    private Byte stockStatus;
    /** 配送状态 */
    private Byte deliveryStatus;
    /** 配送状态 */
    private String deliveryStatusStr;
    /** 配送单编码 */
    private String orderNo;
    /** 业务类型 */
    private Byte orderType;
    /** 门店Code*/
    private String shopCode;
}
