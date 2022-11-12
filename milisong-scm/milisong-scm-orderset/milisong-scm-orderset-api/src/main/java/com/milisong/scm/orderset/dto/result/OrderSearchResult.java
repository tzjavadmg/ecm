package com.milisong.scm.orderset.dto.result;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.milisong.scm.orderset.annotation.Export;
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
public class OrderSearchResult implements Serializable {
    private static final long serialVersionUID = -8521710995401914959L;
    /** 子集单id */
    private Long id;
    /** 配送单 */
    private String description;
    /** 门店id */
    private Long shopId;
    /** 门店名称 */
    private String shopName;
    /** 顾客联编号 */
    private String coupletNo;
    /** 期望到达时间 */
    private String expectArriveTime;
    /** 实际到达时间 */
    private String arriveTime;
    /** 下单日期 */
    private String orderDate;
    /** 用户信息 用户名_手机 */
    private String userInfo;
    /** 配送地址 */
    private String deliveryAddress;
    /** 商品名称 */
    private List<OrderGoodsResult> goodsResultList;
    /** 付款金额 */
    private BigDecimal actualPayAmount;
    /** 配送状态 */
    private Byte deliveryStatus;
    /** 配送状态 */
    private String deliveryStatusStr;
    /** 配送单编码 */
    private String orderNo;
    /** 取餐点 */
    private String mealAddress;

}
