package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.util.Date;

public class SendRedpacketWater implements Serializable {
    private Long id;

    private Long sendRedpacketRecordId;

    private Long userId;

    private String userName;

    private String mobileNo;

    private Byte sex;

    private String deliveryCompany;

    private Date sendTime;

    private Byte businessLine;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSendRedpacketRecordId() {
        return sendRedpacketRecordId;
    }

    public void setSendRedpacketRecordId(Long sendRedpacketRecordId) {
        this.sendRedpacketRecordId = sendRedpacketRecordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
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

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany == null ? null : deliveryCompany.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SendRedpacketWater other = (SendRedpacketWater) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSendRedpacketRecordId() == null ? other.getSendRedpacketRecordId() == null : this.getSendRedpacketRecordId().equals(other.getSendRedpacketRecordId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getMobileNo() == null ? other.getMobileNo() == null : this.getMobileNo().equals(other.getMobileNo()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getDeliveryCompany() == null ? other.getDeliveryCompany() == null : this.getDeliveryCompany().equals(other.getDeliveryCompany()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
            && (this.getBusinessLine() == null ? other.getBusinessLine() == null : this.getBusinessLine().equals(other.getBusinessLine()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSendRedpacketRecordId() == null) ? 0 : getSendRedpacketRecordId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getMobileNo() == null) ? 0 : getMobileNo().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getDeliveryCompany() == null) ? 0 : getDeliveryCompany().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getBusinessLine() == null) ? 0 : getBusinessLine().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sendRedpacketRecordId=").append(sendRedpacketRecordId);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", mobileNo=").append(mobileNo);
        sb.append(", sex=").append(sex);
        sb.append(", deliveryCompany=").append(deliveryCompany);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", businessLine=").append(businessLine);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}