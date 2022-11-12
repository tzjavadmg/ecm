package com.milisong.ecm.common.user.domain;


import java.io.Serializable;
import java.util.Date;

public class UserDeliveryAddress implements Serializable {

    private Long id;

    private Long userId;

    private String realName;

    private String mobileNo;

    private Byte sex;

    private Long deliveryOfficeBuildingId;

    private String deliveryCompany;

    private String deliveryAddress;

    private String deliveryFloor;

    private String deliveryRoom;

    private Date createTime;

    private Date updateTime;
    
    private Integer notifyType;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Long getDeliveryOfficeBuildingId() {
        return deliveryOfficeBuildingId;
    }

    public void setDeliveryOfficeBuildingId(Long deliveryOfficeBuildingId) {
        this.deliveryOfficeBuildingId = deliveryOfficeBuildingId;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany == null ? null : deliveryCompany.trim();
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress == null ? null : deliveryAddress.trim();
    }

    public String getDeliveryFloor() {
        return deliveryFloor;
    }

    public void setDeliveryFloor(String deliveryFloor) {
        this.deliveryFloor = deliveryFloor == null ? null : deliveryFloor.trim();
    }

    public String getDeliveryRoom() {
        return deliveryRoom;
    }

    public void setDeliveryRoom(String deliveryRoom) {
        this.deliveryRoom = deliveryRoom == null ? null : deliveryRoom.trim();
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

	public Integer getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(Integer notifyType) {
		this.notifyType = notifyType;
	}
}
