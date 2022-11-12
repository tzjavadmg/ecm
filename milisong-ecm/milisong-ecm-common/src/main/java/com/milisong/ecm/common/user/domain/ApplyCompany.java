package com.milisong.ecm.common.user.domain;

import java.util.Date;

/**
 * @description:apply_company表的实体类
 * @version:  1.0
 * @author:  admin
 * @createTime: 2019-01-24 14:18:58
 */
public class ApplyCompany {
    private Long id;

    /**
     * 业务线:0-早饭,1-午饭,2-下午茶,3-晚餐,4-夜宵
     */
    private Byte businessLine;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 联系人手机号
     */
    private String mobileNo;

    /**
     * 目标名称（公司或大楼）
     */
    private String itemName;

    /**
     * 目标人数
     */
    private String itemPeople;

    /**
     * 备注
     */
    private String remark;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getItemPeople() {
        return itemPeople;
    }

    public void setItemPeople(String itemPeople) {
        this.itemPeople = itemPeople == null ? null : itemPeople.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}