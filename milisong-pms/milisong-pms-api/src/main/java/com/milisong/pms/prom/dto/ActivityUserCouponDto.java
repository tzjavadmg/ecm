package com.milisong.pms.prom.dto;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ActivityUserCouponDto implements Serializable {
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

    private BigDecimal discount;

    private Byte players;

    private Byte valid;

    private Byte isShare;

    private Byte clickLimit;

    private Date startDate;

    private Date endDate;

    private Byte businessLine;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    private String createTimeText;

    public String getCreateTimeText() {
        if (createTime != null){
            return new DateTime(createTime).toString("MM.dd HH:mm:ss");
        }
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ActivityUserCouponDto{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", type=" + type +
                ", launchType=" + launchType +
                ", launchId=" + launchId +
                ", userId=" + userId +
                ", openId='" + openId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headPortraitUrl='" + headPortraitUrl + '\'' +
                ", clickCount=" + clickCount +
                ", isSuccess=" + isSuccess +
                ", discount=" + discount +
                ", players=" + players +
                ", valid=" + valid +
                ", isShare=" + isShare +
                ", clickLimit=" + clickLimit +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", businessLine=" + businessLine +
                '}';
    }
}