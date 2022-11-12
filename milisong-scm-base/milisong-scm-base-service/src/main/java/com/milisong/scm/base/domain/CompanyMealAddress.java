package com.milisong.scm.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 早餐-公司取餐点信息
 */
@Getter
@Setter
public class CompanyMealAddress {
    /**
     * 
     */
    private Long id;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 取餐点名称
     */
    private String mealAddress;

    /**
     * 取餐点图片
     */
    private String picture;

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