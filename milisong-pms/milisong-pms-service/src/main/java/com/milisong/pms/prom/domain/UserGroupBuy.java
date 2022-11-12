package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.util.Date;

public class UserGroupBuy implements Serializable {
    private Long id;

    private Long activityGroupBuyId;

    private Long originUserId;

    private Long companyId;

    private Byte status;

    private Integer minJoinNum;

    private Integer validPeriod;

    private Date startDate;

    private Date endDate;

    private Date successTime;

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

    public Long getActivityGroupBuyId() {
        return activityGroupBuyId;
    }

    public void setActivityGroupBuyId(Long activityGroupBuyId) {
        this.activityGroupBuyId = activityGroupBuyId;
    }

    public Long getOriginUserId() {
        return originUserId;
    }

    public void setOriginUserId(Long originUserId) {
        this.originUserId = originUserId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getMinJoinNum() {
        return minJoinNum;
    }

    public void setMinJoinNum(Integer minJoinNum) {
        this.minJoinNum = minJoinNum;
    }

    public Integer getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(Integer validPeriod) {
        this.validPeriod = validPeriod;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
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
        UserGroupBuy other = (UserGroupBuy) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getActivityGroupBuyId() == null ? other.getActivityGroupBuyId() == null : this.getActivityGroupBuyId().equals(other.getActivityGroupBuyId()))
            && (this.getOriginUserId() == null ? other.getOriginUserId() == null : this.getOriginUserId().equals(other.getOriginUserId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getMinJoinNum() == null ? other.getMinJoinNum() == null : this.getMinJoinNum().equals(other.getMinJoinNum()))
            && (this.getValidPeriod() == null ? other.getValidPeriod() == null : this.getValidPeriod().equals(other.getValidPeriod()))
            && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getSuccessTime() == null ? other.getSuccessTime() == null : this.getSuccessTime().equals(other.getSuccessTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getActivityGroupBuyId() == null) ? 0 : getActivityGroupBuyId().hashCode());
        result = prime * result + ((getOriginUserId() == null) ? 0 : getOriginUserId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getMinJoinNum() == null) ? 0 : getMinJoinNum().hashCode());
        result = prime * result + ((getValidPeriod() == null) ? 0 : getValidPeriod().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getSuccessTime() == null) ? 0 : getSuccessTime().hashCode());
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
        sb.append(", activityGroupBuyId=").append(activityGroupBuyId);
        sb.append(", originUserId=").append(originUserId);
        sb.append(", companyId=").append(companyId);
        sb.append(", status=").append(status);
        sb.append(", minJoinNum=").append(minJoinNum);
        sb.append(", validPeriod=").append(validPeriod);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", successTime=").append(successTime);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}