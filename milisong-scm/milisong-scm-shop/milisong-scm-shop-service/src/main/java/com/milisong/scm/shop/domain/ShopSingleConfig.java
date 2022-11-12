package com.milisong.scm.shop.domain;

import java.util.Date;
import lombok.Data;

/**
 * @description:t_shop_single_config表的实体类
 * @version:  1.1
 * @author:  田海波
 * @createTime: 2018-11-20 10:42:11
 */
@Data
public class ShopSingleConfig {
    private Long id;

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
     * 业务线：1-ecm，2-scm，3-pos，0-common
     */
    private String serviceLine;

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
     * 配置值的类型 1字符串 2整数 3小数 4日期 5时间 6日期+时间
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