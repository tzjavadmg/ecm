package com.milisong.breakfast.scm.order.dto.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *  订单详情查询参数封装
 * @author zhaozhonghui
 * @date 2018-11-20
 */
@Getter
@Setter
public class OrderSearchParam extends PageParam implements Serializable {

    private static final long serialVersionUID = 5853779082190654160L;
    /** 门店编码 */
    @ApiModelProperty("门店Id")
    private String shopId;
    /** 配送开始日期 yyyy-MM-dd */
    @ApiModelProperty("配送开始日期")
    private String deliveryStartDate;
    /** 配送结束日期 yyyy-MM-dd  */
    @ApiModelProperty("配送结束日期")
    private String deliveryEndDate;
    /** 用户手机号 或者 用户姓名 */
    @ApiModelProperty("用户手机或者姓名")
    private String userParam;
    /** 配送状态  0 待接单 1已接单 2已到店  3已取餐 4已完成 */
    @ApiModelProperty("配送状态 0 待接单 1已接单 2已到店  3已取餐 4已完成 ")
    private Byte deliveryStatus;
    /** 查询订单开始日期 */
    @ApiModelProperty("订单开始日期")
    private String orderStartDate;
    /** 查询订单结束日期  */
    @ApiModelProperty("订单结束日期")
    private String orderEndDate;
    @ApiModelProperty("订单类型")
    private Byte orderType;

}
