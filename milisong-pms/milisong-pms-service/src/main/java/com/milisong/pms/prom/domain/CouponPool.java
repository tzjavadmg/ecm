package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CouponPool implements Serializable {
    private Long id;

    private Long activityId;

    private Long userActivityId;

    private Long userId;

    private String nickName;

    private String headPortraitUrl;

    private String couponName;

    private Byte couponType;

    private Integer limitDays;

    private String goodsName;

    private String goodsCode;

    private BigDecimal discount;

    private Byte isShare;

    private Integer validDays;

    private String rule;

    private Byte status;

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

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Long userActivityId) {
        this.userActivityId = userActivityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Byte getCouponType() {
        return couponType;
    }

    public void setCouponType(Byte couponType) {
        this.couponType = couponType;
    }

    public Integer getLimitDays() {
        return limitDays;
    }

    public void setLimitDays(Integer limitDays) {
        this.limitDays = limitDays;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
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

    public Byte getIsShare() {
        return isShare;
    }

    public void setIsShare(Byte isShare) {
        this.isShare = isShare;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
        CouponPool other = (CouponPool) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getActivityId() == null ? other.getActivityId() == null : this.getActivityId().equals(other.getActivityId()))
            && (this.getUserActivityId() == null ? other.getUserActivityId() == null : this.getUserActivityId().equals(other.getUserActivityId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getHeadPortraitUrl() == null ? other.getHeadPortraitUrl() == null : this.getHeadPortraitUrl().equals(other.getHeadPortraitUrl()))
            && (this.getCouponName() == null ? other.getCouponName() == null : this.getCouponName().equals(other.getCouponName()))
            && (this.getCouponType() == null ? other.getCouponType() == null : this.getCouponType().equals(other.getCouponType()))
            && (this.getLimitDays() == null ? other.getLimitDays() == null : this.getLimitDays().equals(other.getLimitDays()))
            && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
            && (this.getGoodsCode() == null ? other.getGoodsCode() == null : this.getGoodsCode().equals(other.getGoodsCode()))
            && (this.getDiscount() == null ? other.getDiscount() == null : this.getDiscount().equals(other.getDiscount()))
            && (this.getIsShare() == null ? other.getIsShare() == null : this.getIsShare().equals(other.getIsShare()))
            && (this.getValidDays() == null ? other.getValidDays() == null : this.getValidDays().equals(other.getValidDays()))
            && (this.getRule() == null ? other.getRule() == null : this.getRule().equals(other.getRule()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBusinessLine() == null ? other.getBusinessLine() == null : this.getBusinessLine().equals(other.getBusinessLine()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getActivityId() == null) ? 0 : getActivityId().hashCode());
        result = prime * result + ((getUserActivityId() == null) ? 0 : getUserActivityId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getHeadPortraitUrl() == null) ? 0 : getHeadPortraitUrl().hashCode());
        result = prime * result + ((getCouponName() == null) ? 0 : getCouponName().hashCode());
        result = prime * result + ((getCouponType() == null) ? 0 : getCouponType().hashCode());
        result = prime * result + ((getLimitDays() == null) ? 0 : getLimitDays().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getGoodsCode() == null) ? 0 : getGoodsCode().hashCode());
        result = prime * result + ((getDiscount() == null) ? 0 : getDiscount().hashCode());
        result = prime * result + ((getIsShare() == null) ? 0 : getIsShare().hashCode());
        result = prime * result + ((getValidDays() == null) ? 0 : getValidDays().hashCode());
        result = prime * result + ((getRule() == null) ? 0 : getRule().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", activityId=").append(activityId);
        sb.append(", userActivityId=").append(userActivityId);
        sb.append(", userId=").append(userId);
        sb.append(", nickName=").append(nickName);
        sb.append(", headPortraitUrl=").append(headPortraitUrl);
        sb.append(", couponName=").append(couponName);
        sb.append(", couponType=").append(couponType);
        sb.append(", limitDays=").append(limitDays);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", goodsCode=").append(goodsCode);
        sb.append(", discount=").append(discount);
        sb.append(", isShare=").append(isShare);
        sb.append(", validDays=").append(validDays);
        sb.append(", rule=").append(rule);
        sb.append(", status=").append(status);
        sb.append(", businessLine=").append(businessLine);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}