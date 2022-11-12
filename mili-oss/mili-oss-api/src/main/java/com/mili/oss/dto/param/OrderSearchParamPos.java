package com.mili.oss.dto.param;

import com.farmland.core.api.PageParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaozhonghui
 * @Description 集单管理--订单查询参数
 * @date 2019-03-21
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderSearchParamPos extends PageParam implements Serializable {
    private static final long serialVersionUID = -3845130598406393537L;
    /** 业务类型 0早餐 1午餐*/
    private Byte orderType;
    /** 配送日期 */
    private String deliveryDate;
    /** 配送状态 */
    private Byte deliveryStatus;
    /** 配送单号 */
    private String description;
    /** 配送单号集合 */
    private List<String> descriptionList;
    /** 顾客联 */
    private List<String> coupletNo;
    /** 用户信息 */
    private String user;
    /** 商品信息 */
    private String goods;
    /** 备注 */
    private Byte stockStatus;
    /** 门店id */
    private String shopId;
}
