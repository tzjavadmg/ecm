package com.milisong.scm.base.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 早餐-公司取餐时间信息
 */
@Getter
@Setter
public class CompanyMealTime {
    /**
     * 
     */
    private Long id;

    /**
     * 去重编码
     */
    private Integer code;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 配送时间begin
     */
    private Date deliveryTimeBegin;

    /**
     * 配送时间end
     */
    private Date deliveryTimeEnd;

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

    /**
     * 业务线 0早餐 1午餐
     */
    private Byte businessLine;
}