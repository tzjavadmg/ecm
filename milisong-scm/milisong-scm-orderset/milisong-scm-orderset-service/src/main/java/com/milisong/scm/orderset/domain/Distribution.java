package com.milisong.scm.orderset.domain;

import java.util.Date;

public class Distribution {
    private Long id;

    private Long shopId;

    private String distributionNo;

    private String distributionDescription;

    private Integer ordersetSum;

    private Integer goodsSum;

    private String distributorAccount;

    private String distributorName;

    private Date deliveryDate;

    private Date theoryDeliveryStartDate;

    private Date theoryDeliveryEndDate;

    private Date actualDeliveryStartDate;

    private Date actualDeliveryEndDate;

    private Byte status;

    private Boolean isPrintPickList;

    private Boolean isPrintDistribution;

    private Boolean isDeleted;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getDistributionNo() {
        return distributionNo;
    }

    public void setDistributionNo(String distributionNo) {
        this.distributionNo = distributionNo == null ? null : distributionNo.trim();
    }

    public String getDistributionDescription() {
        return distributionDescription;
    }

    public void setDistributionDescription(String distributionDescription) {
        this.distributionDescription = distributionDescription == null ? null : distributionDescription.trim();
    }

    public Integer getOrdersetSum() {
        return ordersetSum;
    }

    public void setOrdersetSum(Integer ordersetSum) {
        this.ordersetSum = ordersetSum;
    }

    public Integer getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(Integer goodsSum) {
        this.goodsSum = goodsSum;
    }

    public String getDistributorAccount() {
        return distributorAccount;
    }

    public void setDistributorAccount(String distributorAccount) {
        this.distributorAccount = distributorAccount == null ? null : distributorAccount.trim();
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName == null ? null : distributorName.trim();
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getTheoryDeliveryStartDate() {
        return theoryDeliveryStartDate;
    }

    public void setTheoryDeliveryStartDate(Date theoryDeliveryStartDate) {
        this.theoryDeliveryStartDate = theoryDeliveryStartDate;
    }

    public Date getTheoryDeliveryEndDate() {
        return theoryDeliveryEndDate;
    }

    public void setTheoryDeliveryEndDate(Date theoryDeliveryEndDate) {
        this.theoryDeliveryEndDate = theoryDeliveryEndDate;
    }

    public Date getActualDeliveryStartDate() {
        return actualDeliveryStartDate;
    }

    public void setActualDeliveryStartDate(Date actualDeliveryStartDate) {
        this.actualDeliveryStartDate = actualDeliveryStartDate;
    }

    public Date getActualDeliveryEndDate() {
        return actualDeliveryEndDate;
    }

    public void setActualDeliveryEndDate(Date actualDeliveryEndDate) {
        this.actualDeliveryEndDate = actualDeliveryEndDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Boolean getIsPrintPickList() {
        return isPrintPickList;
    }

    public void setIsPrintPickList(Boolean isPrintPickList) {
        this.isPrintPickList = isPrintPickList;
    }

    public Boolean getIsPrintDistribution() {
        return isPrintDistribution;
    }

    public void setIsPrintDistribution(Boolean isPrintDistribution) {
        this.isPrintDistribution = isPrintDistribution;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}