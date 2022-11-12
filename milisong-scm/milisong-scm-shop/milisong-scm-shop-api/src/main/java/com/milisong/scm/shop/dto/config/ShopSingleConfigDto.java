package com.milisong.scm.shop.dto.config;

import lombok.Data;

import java.util.Date;

/**
 * @description:门店属性配置Dto
 * @version  1.0
 * @author:  tianhaibo
 * @createTime: 2018-10-23 17:19:32
 */
@Data
public class ShopSingleConfigDto {
    private Long id;
    /**
     * 业务线：1-ecm，2-scm，3-pos，0-common
     */
    private String[] serviceLines;

    /**
     * 门店d
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
     * 配置key
     */
    private String configKey;

    /**
     * 配置key描述
     */
    private String configDescribe;

    /**
     * 配置value
     */
    private String configValue;

    /**
     * 配置值的类型 1字符串 2数字 3日期 4时间 5日期+时间
     */
    private Byte configValueType;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人
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