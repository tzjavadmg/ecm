package com.mili.oss.dto.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description:截单配置响应Dto
 * @version  1.0
 * @author:  tianhaibo
 * @createTime: 2018-10-23 17:19:32
 */
@Data
public class ShopInterceptConfigDto {
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
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date orderInterceptTime;

    /**
     * 第一次集单时间
     */
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date firstOrdersetTime;

    /**
     * 第二次集单时间
     */
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date secondOrdersetTime;

    /**
     * 配送开始时间
     */
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeBegin;

    /**
     * 配送结束时间
     */
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeEnd;
    /**
     * 派单时间
     */
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
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

    private Integer spaceTime = 5;

}