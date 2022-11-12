package com.milisong.scm.orderset.dto.result;

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
public class OrderExportResult implements Serializable {
    private static final long serialVersionUID = 5616704675820724223L;
    /** 子集单id */
    private Long id;
    /** 配送单 */
    @Export(value = "配送单",rank = 0)
    private String description;
    /** 门店id */
    private Long shopId;
    /** 门店名称 */
    @Export(value = "门店",rank = 1)
    private String shopName;
    /** 顾客联编号 */
    @Export(value = "顾客联编号",rank = 2)
    private String coupletNo;
    /** 期望到达时间 */
    @Export(value = "期望到达时间",rank = 10)
    private String expectArriveTime;
    /** 实际到达时间 */
    @Export(value = "实际到达时间",rank = 20)
    private String arriveTime;
    /** 下单日期 */
    @Export(value = "下单日期",rank = 30)
    private String orderDate;
    /** 用户信息 用户名_手机 */
    @Export(value = "客户信息",rank = 40)
    private String userInfo;
    /** 配送地址 */
    @Export(value = "配送地址",rank = 50)
    private String deliveryAddress;
    @Export(value = "取餐点",rank = 51)
    private String mealAddress;
    /** 商品 */
    private List<OrderGoodsResult> goodsResultList;
    @Export(value = "商品",rank = 60)
    private String goodsInfo;
//    @Export(value = "商品总数",rank = 61)
    private int goodsTotalNumber;
    /** 付款金额 */
    @Export(value = "支付总金额",rank = 70)
    private BigDecimal actualPayAmount;
    /** 配送状态 */
    private Byte deliveryStatus;
    /** 配送状态 */
    @Export(value = "配送状态",rank = 80)
    private String deliveryStatusStr;
    /** 配送单编码 */
    private String orderNo;
}
