package com.milisong.scm.goods.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 午餐-商品档期主表
 */
@Getter
@Setter
public class GoodsScheduleLunch {
    /**
     * 
     */
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
     * 年份
     */
    private Integer year;

    /**
     * W多少周
     */
    private Integer weekOfYear;

    /**
     * 档期日期
     */
    private Date scheduleDate;

    /**
     * 周几
     */
    private Integer week;

    /**
     * 是可可售 1可售 0不可售
     */
    private Boolean status;

    /**
     * 发布时间(生效时间)
     */
    private Date publishTime;

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