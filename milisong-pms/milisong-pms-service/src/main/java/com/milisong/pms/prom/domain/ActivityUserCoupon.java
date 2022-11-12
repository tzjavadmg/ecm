package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.util.Date;

public class ActivityUserCoupon implements Serializable {
    private Long id;

    private Long activityId;

    private Byte type;

    private Byte launchType;

    private Long launchId;

    private Long userId;

    private String openId;

    private String nickName;

    private String headPortraitUrl;

    private Byte clickCount;

    private Byte isSuccess;

    private Byte players;

    private Byte valid;

    private Byte isShare;

    private Byte clickLimit;

    private Date startDate;

    private Date endDate;

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getLaunchType() {
        return launchType;
    }

    public void setLaunchType(Byte launchType) {
        this.launchType = launchType;
    }

    public Long getLaunchId() {
        return launchId;
    }

    public void setLaunchId(Long launchId) {
        this.launchId = launchId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
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

    public Byte getClickCount() {
        return clickCount;
    }

    public void setClickCount(Byte clickCount) {
        this.clickCount = clickCount;
    }

    public Byte getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Byte isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Byte getPlayers() {
        return players;
    }

    public void setPlayers(Byte players) {
        this.players = players;
    }

    public Byte getValid() {
        return valid;
    }

    public void setValid(Byte valid) {
        this.valid = valid;
    }

    public Byte getIsShare() {
        return isShare;
    }

    public void setIsShare(Byte isShare) {
        this.isShare = isShare;
    }

    public Byte getClickLimit() {
        return clickLimit;
    }

    public void setClickLimit(Byte clickLimit) {
        this.clickLimit = clickLimit;
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
        ActivityUserCoupon other = (ActivityUserCoupon) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getActivityId() == null ? other.getActivityId() == null : this.getActivityId().equals(other.getActivityId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getLaunchType() == null ? other.getLaunchType() == null : this.getLaunchType().equals(other.getLaunchType()))
            && (this.getLaunchId() == null ? other.getLaunchId() == null : this.getLaunchId().equals(other.getLaunchId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOpenId() == null ? other.getOpenId() == null : this.getOpenId().equals(other.getOpenId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getHeadPortraitUrl() == null ? other.getHeadPortraitUrl() == null : this.getHeadPortraitUrl().equals(other.getHeadPortraitUrl()))
            && (this.getClickCount() == null ? other.getClickCount() == null : this.getClickCount().equals(other.getClickCount()))
            && (this.getIsSuccess() == null ? other.getIsSuccess() == null : this.getIsSuccess().equals(other.getIsSuccess()))
            && (this.getPlayers() == null ? other.getPlayers() == null : this.getPlayers().equals(other.getPlayers()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()))
            && (this.getIsShare() == null ? other.getIsShare() == null : this.getIsShare().equals(other.getIsShare()))
            && (this.getClickLimit() == null ? other.getClickLimit() == null : this.getClickLimit().equals(other.getClickLimit()))
            && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
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
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getLaunchType() == null) ? 0 : getLaunchType().hashCode());
        result = prime * result + ((getLaunchId() == null) ? 0 : getLaunchId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOpenId() == null) ? 0 : getOpenId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getHeadPortraitUrl() == null) ? 0 : getHeadPortraitUrl().hashCode());
        result = prime * result + ((getClickCount() == null) ? 0 : getClickCount().hashCode());
        result = prime * result + ((getIsSuccess() == null) ? 0 : getIsSuccess().hashCode());
        result = prime * result + ((getPlayers() == null) ? 0 : getPlayers().hashCode());
        result = prime * result + ((getValid() == null) ? 0 : getValid().hashCode());
        result = prime * result + ((getIsShare() == null) ? 0 : getIsShare().hashCode());
        result = prime * result + ((getClickLimit() == null) ? 0 : getClickLimit().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", launchType=").append(launchType);
        sb.append(", launchId=").append(launchId);
        sb.append(", userId=").append(userId);
        sb.append(", openId=").append(openId);
        sb.append(", nickName=").append(nickName);
        sb.append(", headPortraitUrl=").append(headPortraitUrl);
        sb.append(", clickCount=").append(clickCount);
        sb.append(", isSuccess=").append(isSuccess);
        sb.append(", players=").append(players);
        sb.append(", valid=").append(valid);
        sb.append(", isShare=").append(isShare);
        sb.append(", clickLimit=").append(clickLimit);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", businessLine=").append(businessLine);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}