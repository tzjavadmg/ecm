package com.milisong.scm.stock.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 门店可售商品量配置表
 */
@Getter
@Setter
public class ShopOnsaleGoodsStock {
    /**
     * 
     */
    private Long id;

    /**
     * 门店可售商品表记录的id
     */
    private Long relationId;

    /**
     * 可售日期
     */
    private Date saleDate;

    /**
     * 可售数量
     */
    private Integer availableVolume;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人  账号_名字组合
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改人 账号_名字组合
     */
    private String updateBy;

    /**
     * 最后修改时间
     */
    private Date updateTime;
}