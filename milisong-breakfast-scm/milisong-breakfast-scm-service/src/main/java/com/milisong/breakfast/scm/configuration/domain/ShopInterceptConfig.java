package com.milisong.breakfast.scm.configuration.domain;

import java.util.Date;
import lombok.Data;

/**
 * @description:shop_intercept_config表的实体类
 * @version:  1.0
 * @author:  admin
 * @createTime: 2019-01-25 11:00:04
 */
@Data
public class ShopInterceptConfig {
    private Long id;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 门店code
     */
    private String shopCode;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 订单截单时间
     */
    private Date orderInterceptTime;

    /**
     * 第一次集单时间
     */
    private Date firstOrdersetTime;

    /**
     * 第二次集单时间
     */
    private Date secondOrdersetTime;

    /**
     * 配送开始时间
     */
    private Date deliveryTimeBegin;

    /**
     * 配送结束时间
     */
    private Date deliveryTimeEnd;

    /**
     * 派单时间
     */
    private Date dispatchTime;

    /**
     * 是否默认配送设置 1是 0否
     */
    private Boolean isDefault;

    /**
     * 最大生产量
     */
    private Integer maxOutput;

    /**
     * 状态 1有效 0无效
     */
    private Byte status;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;
}