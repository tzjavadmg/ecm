package com.milisong.pos.production.domain;

import java.math.BigDecimal;
import java.util.Date;

public class OrderSetDetail {
    private Long id;

    private String setNo;

    private String detailSetNo;

    private String detailSetNoDescription;

    private Date detailDeliveryDate;

    private Date deliveryStartTime;

    private Date deliveryEndTime;

    private String detailDeliveryAddress;

    private Long shopId;

    private Long buildingId;

    private String buildingAbbreviation;

    private String deliveryFloor;

    private String mealAddress;

    private Long companyId;

    private String companyAbbreviation;

    private Integer goodsSum;

    private BigDecimal actualPayAmount;

    private Boolean isPrint;

    private Byte status;

    private Boolean isDeleted;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSetNo() {
        return setNo;
    }

    public void setSetNo(String setNo) {
        this.setNo = setNo == null ? null : setNo.trim();
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

    public Date getDetailDeliveryDate() {
        return detailDeliveryDate;
    }

    public void setDetailDeliveryDate(Date detailDeliveryDate) {
        this.detailDeliveryDate = detailDeliveryDate;
    }

    public Date getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryStartTime(Date deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    public Date getDeliveryEndTime() {
        return deliveryEndTime;
    }

    public void setDeliveryEndTime(Date deliveryEndTime) {
        this.deliveryEndTime = deliveryEndTime;
    }

    public String getDetailDeliveryAddress() {
        return detailDeliveryAddress;
    }

    public void setDetailDeliveryAddress(String detailDeliveryAddress) {
        this.detailDeliveryAddress = detailDeliveryAddress == null ? null : detailDeliveryAddress.trim();
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public String getMealAddress() {
        return mealAddress;
    }

    public void setMealAddress(String mealAddress) {
        this.mealAddress = mealAddress == null ? null : mealAddress.trim();
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyAbbreviation() {
        return companyAbbreviation;
    }

    public void setCompanyAbbreviation(String companyAbbreviation) {
        this.companyAbbreviation = companyAbbreviation == null ? null : companyAbbreviation.trim();
    }

    public Integer getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(Integer goodsSum) {
        this.goodsSum = goodsSum;
    }

    public BigDecimal getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(BigDecimal actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public Boolean getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Boolean isPrint) {
        this.isPrint = isPrint;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
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