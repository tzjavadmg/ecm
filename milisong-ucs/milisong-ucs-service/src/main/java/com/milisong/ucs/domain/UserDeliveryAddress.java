package com.milisong.ucs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:t_user_delivery_address表的实体类
 * @version:  1.0
 * @author:  田海波
 * @createTime: 2019-02-11 14:40:31
 */
public class UserDeliveryAddress implements Serializable {
    private static final long serialVersionUID = -2525626532739917667L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 性别(0:女;1:男)
     */
    private Byte sex;

    /**
     * 楼宇ID
     */
    private Long deliveryOfficeBuildingId;

    /**
     * 公司名称
     */
    private String deliveryCompany;

    /**
     * 详细地址
     */
    private String deliveryAddress;

    /**
     * 楼层
     */
    private String deliveryFloor;

    /**
     * 房间号
     */
    private String deliveryRoom;

    /**
     * 业务线：0 早餐、1  午餐、2 下午茶、3 晚餐、4 夜宵
     */
    private Byte businessLine;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 通知类型 0:电话,1:短信
     */
    private Integer notifyType;

    /**
     * 取餐点id
     */
    private Long takeMealsAddrId;

    /**
     * 取餐点名称
     */
    private String takeMealsAddrName;

    /**
     * 配送时间id
     */
    private Long deliveryTimeId;

    /**
     * 配送开始时间
     */
    private Date deliveryTimeBegin;

    /**
     * 配送结束时间
     */
    private Date deliveryTimeEnd;

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

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
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

    public Long getTakeMealsAddrId() {
        return takeMealsAddrId;
    }

    public void setTakeMealsAddrId(Long takeMealsAddrId) {
        this.takeMealsAddrId = takeMealsAddrId;
    }

    public String getTakeMealsAddrName() {
        return takeMealsAddrName;
    }

    public void setTakeMealsAddrName(String takeMealsAddrName) {
        this.takeMealsAddrName = takeMealsAddrName == null ? null : takeMealsAddrName.trim();
    }

    public Long getDeliveryTimeId() {
        return deliveryTimeId;
    }

    public void setDeliveryTimeId(Long deliveryTimeId) {
        this.deliveryTimeId = deliveryTimeId;
    }

    public Date getDeliveryTimeBegin() {
        return deliveryTimeBegin;
    }

    public void setDeliveryTimeBegin(Date deliveryTimeBegin) {
        this.deliveryTimeBegin = deliveryTimeBegin;
    }

    public Date getDeliveryTimeEnd() {
        return deliveryTimeEnd;
    }

    public void setDeliveryTimeEnd(Date deliveryTimeEnd) {
        this.deliveryTimeEnd = deliveryTimeEnd;
    }
}