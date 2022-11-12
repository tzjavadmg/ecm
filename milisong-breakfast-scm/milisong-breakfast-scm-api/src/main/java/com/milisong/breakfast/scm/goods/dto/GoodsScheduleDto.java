package com.milisong.breakfast.scm.goods.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月3日---下午7:49:22
*
*/
@Getter
@Setter
public class GoodsScheduleDto {

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
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
