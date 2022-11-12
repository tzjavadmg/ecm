package com.milisong.scm.orderset.domain;

import java.util.Date;

public class DistributionOrderset {
    private Long id;

    private String distributionNo;

    private String distributionDescription;

    private Date deliveryDate;

    private Long buildingId;

    private String buildingAbbreviation;

    private String deliveryFloor;

    private String companyAbbreviation;

    private String detailSetNo;

    private String detailSetNoDescription;

    private Integer goodsSum;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingAbbreviation() {
        return buildingAbbreviation;
    }

    public void setBuildingAbbreviation(String buildingAbbreviation) {
        this.buildingAbbreviation = buildingAbbreviation == null ? null : buildingAbbreviation.trim();
    }

    public String getDeliveryFloor() {
        return deliveryFloor;
    }

    public void setDeliveryFloor(String deliveryFloor) {
        this.deliveryFloor = deliveryFloor == null ? null : deliveryFloor.trim();
    }

    public String getCompanyAbbreviation() {
        return companyAbbreviation;
    }

    public void setCompanyAbbreviation(String companyAbbreviation) {
        this.companyAbbreviation = companyAbbreviation == null ? null : companyAbbreviation.trim();
    }

    public String getDetailSetNo() {
        return detailSetNo;
    }

    public void setDetailSetNo(String detailSetNo) {
        this.detailSetNo = detailSetNo == null ? null : detailSetNo.trim();
    }

    public String getDetailSetNoDescription() {
        return detailSetNoDescription;
    }

    public void setDetailSetNoDescription(String detailSetNoDescription) {
        this.detailSetNoDescription = detailSetNoDescription == null ? null : detailSetNoDescription.trim();
    }

    public Integer getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(Integer goodsSum) {
        this.goodsSum = goodsSum;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}