package com.mili.oss.dto.param;

import com.farmland.core.api.PageParam;
import lombok.Data;

import java.io.Serializable;

/**
 *  订单详情查询参数封装
 * @author zhaozhonghui
 * @date 2018-11-20
 */
@Data
public class OrderSearchParam extends PageParam implements Serializable {

    private static final long serialVersionUID = 5853779082190654160L;
    /** 门店编码 */
    private String shopId;
    /** 配送开始日期 yyyy-MM-dd */
    private String deliveryStartDate;
    /** 配送结束日期 yyyy-MM-dd  */
    private String deliveryEndDate;
    /** 用户手机号 或者 用户姓名 */
    private String userParam;
    /** 配送状态  0 待接单 1已接单 2已到店  3已取餐 4已完成 */
    private Byte deliveryStatus;
    /** 查询订单开始日期 */
    private String orderStartDate;
    /** 查询订单结束日期  */
    private String orderEndDate;
    /** 订单类型 0早餐 1午餐 */
    private Byte orderType;


}
