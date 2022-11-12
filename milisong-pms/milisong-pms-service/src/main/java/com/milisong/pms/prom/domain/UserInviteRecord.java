package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserInviteRecord implements Serializable {
    private Long id;

    private Long activityInviteId;

    private Long originateUserId;

    private Long receiveUserId;

    private String nickName;

    private String headPortraitUrl;

    private String couponName;

    private String goodsCode;

    private BigDecimal discount;

    private Byte status;

    private Date inviteSuccessTime;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityInviteId() {
        return activityInviteId;
    }

    public void setActivityInviteId(Long activityInviteId) {
        this.activityInviteId = activityInviteId;
    }

    public Long getOriginateUserId() {
        return originateUserId;
    }

    public void setOriginateUserId(Long originateUserId) {
        this.originateUserId = originateUserId;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl == null ? null : headPortraitUrl.trim();
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName == null ? null : couponName.trim();
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getInviteSuccessTime() {
        return inviteSuccessTime;
    }

    public void setInviteSuccessTime(Date inviteSuccessTime) {
        this.inviteSuccessTime = inviteSuccessTime;
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
        UserInviteRecord other = (UserInviteRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getActivityInviteId() == null ? other.getActivityInviteId() == null : this.getActivityInviteId().equals(other.getActivityInviteId()))
            && (this.getOriginateUserId() == null ? other.getOriginateUserId() == null : this.getOriginateUserId().equals(other.getOriginateUserId()))
            && (this.getReceiveUserId() == null ? other.getReceiveUserId() == null : this.getReceiveUserId().equals(other.getReceiveUserId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getHeadPortraitUrl() == null ? other.getHeadPortraitUrl() == null : this.getHeadPortraitUrl().equals(other.getHeadPortraitUrl()))
            && (this.getCouponName() == null ? other.getCouponName() == null : this.getCouponName().equals(other.getCouponName()))
            && (this.getGoodsCode() == null ? other.getGoodsCode() == null : this.getGoodsCode().equals(other.getGoodsCode()))
            && (this.getDiscount() == null ? other.getDiscount() == null : this.getDiscount().equals(other.getDiscount()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getInviteSuccessTime() == null ? other.getInviteSuccessTime() == null : this.getInviteSuccessTime().equals(other.getInviteSuccessTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getActivityInviteId() == null) ? 0 : getActivityInviteId().hashCode());
        result = prime * result + ((getOriginateUserId() == null) ? 0 : getOriginateUserId().hashCode());
        result = prime * result + ((getReceiveUserId() == null) ? 0 : getReceiveUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getHeadPortraitUrl() == null) ? 0 : getHeadPortraitUrl().hashCode());
        result = prime * result + ((getCouponName() == null) ? 0 : getCouponName().hashCode());
        result = prime * result + ((getGoodsCode() == null) ? 0 : getGoodsCode().hashCode());
        result = prime * result + ((getDiscount() == null) ? 0 : getDiscount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getInviteSuccessTime() == null) ? 0 : getInviteSuccessTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
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
        sb.append(", activityInviteId=").append(activityInviteId);
        sb.append(", originateUserId=").append(originateUserId);
        sb.append(", receiveUserId=").append(receiveUserId);
        sb.append(", nickName=").append(nickName);
        sb.append(", headPortraitUrl=").append(headPortraitUrl);
        sb.append(", couponName=").append(couponName);
        sb.append(", goodsCode=").append(goodsCode);
        sb.append(", discount=").append(discount);
        sb.append(", status=").append(status);
        sb.append(", inviteSuccessTime=").append(inviteSuccessTime);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}