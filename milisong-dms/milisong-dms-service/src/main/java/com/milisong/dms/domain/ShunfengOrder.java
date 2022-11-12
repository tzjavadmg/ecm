/* https://github.com/orange1438 */
package com.milisong.dms.domain;

import java.util.Date;

public class ShunfengOrder {
    private Long id;

    private Long sfOrderId;

    private Long shopOrderId;

    private Date expectTime;

    private Date deliveryDate;

    private Date distributeTime;

    private String deliveryAddress;

    private Date confirmTime;

    private String transporterName;

    private String transporterPhone;

    private Date arrivedShopTime;

    private Date achieveGoodTime;

    private Date arrivedTime;

    private Byte businessType;

    private Byte notifyStatus;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSfOrderId() {
        return sfOrderId;
    }

    public void setSfOrderId(Long sfOrderId) {
        this.sfOrderId = sfOrderId;
    }

    public Long getShopOrderId() {
        return shopOrderId;
    }

    public void setShopOrderId(Long shopOrderId) {
        this.shopOrderId = shopOrderId;
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(Date distributeTime) {
        this.distributeTime = distributeTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress == null ? null : deliveryAddress.trim();
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName == null ? null : transporterName.trim();
    }

    public String getTransporterPhone() {
        return transporterPhone;
    }

    public void setTransporterPhone(String transporterPhone) {
        this.transporterPhone = transporterPhone == null ? null : transporterPhone.trim();
    }

    public Date getArrivedShopTime() {
        return arrivedShopTime;
    }

    public void setArrivedShopTime(Date arrivedShopTime) {
        this.arrivedShopTime = arrivedShopTime;
    }

    public Date getAchieveGoodTime() {
        return achieveGoodTime;
    }

    public void setAchieveGoodTime(Date achieveGoodTime) {
        this.achieveGoodTime = achieveGoodTime;
    }

    public Date getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(Date arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public Byte getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Byte businessType) {
        this.businessType = businessType;
    }

    public Byte getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(Byte notifyStatus) {
        this.notifyStatus = notifyStatus;
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