package com.milisong.breakfast.scm.configuration.domain;

import java.util.Date;
import lombok.Data;

/**
 * @description:shop_banner表的实体类
 * @version:  1.0
 * @author:  admin
 * @createTime: 2018-12-01 14:18:57
 */
@Data
public class ShopBanner {
    /**
     * 主键
     */
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
     * 门店轮播图片
     */
    private String picture;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * banner权重，大优先
     */
    private Byte weight;

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
     * 最后修改人
     */
    private String updateBy;

    /**
     * 最后修改时间
     */
    private Date updateTime;
}