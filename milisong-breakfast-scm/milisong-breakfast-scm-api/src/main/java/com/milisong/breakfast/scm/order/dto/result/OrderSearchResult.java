package com.milisong.breakfast.scm.order.dto.result;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaozhonghui
 * @date 2018-11-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderSearchResult implements Serializable {
    private static final long serialVersionUID = -8521710995401914959L;
    /** 子集单id */
    @ApiModelProperty("子集单ID")
    private Long id;
    @ApiModelProperty("顾客联单号")
    private String coupletNo;
    /** 配送单 */
    @ApiModelProperty("配送单号")
    private String description;
    /** 门店id */
    @ApiModelProperty("门店id")
    private Long shopId;
    /** 门店名称 */
    @ApiModelProperty("门店名称")
    private String shopName;
    /** 期望到达时间 */
    @ApiModelProperty("期望送达时间")
    private String expectArriveTime;
    /** 实际到达时间 */
    @ApiModelProperty("实际送达时间")
    private String arriveTime;
    /** 下单日期 */
    @ApiModelProperty("下单日期")
    private String orderDate;
    /** 用户信息 用户名_手机 */
    @ApiModelProperty("用户信息")
    private String userInfo;
    /** 配送地址 */
    @ApiModelProperty("配送地址")
    private String deliveryAddress;
    /** 商品信息 */
    @ApiModelProperty("商品信息")
    private List<OrderGoodsResult> goodsResultList;
    /** 付款金额 */
    @ApiModelProperty("付款金额")
    private BigDecimal actualPayAmount;
    /** 配送状态 */
    @ApiModelProperty(value = "配送状态",hidden = true)
    private Byte deliveryStatus;
    /** 配送状态 */
    @ApiModelProperty("配送状态")
    private String deliveryStatusStr;
    /** 配送单编码 */
    @ApiModelProperty("ecm配送单编码 作为唯一约束")
    private String orderNo;
    @ApiModelProperty("取餐点")
    private String mealAddress;
}
