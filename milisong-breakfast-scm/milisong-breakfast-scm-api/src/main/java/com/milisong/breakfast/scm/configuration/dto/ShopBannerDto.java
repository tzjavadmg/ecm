package com.milisong.breakfast.scm.configuration.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description:全门店Banner配置响应Dto
 * @version  1.0
 * @author:  tianhaibo
 * @createTime: 2018-10-23 17:19:32
 */
@Data
public class ShopBannerDto {
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

    private String actualPicUrl;

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