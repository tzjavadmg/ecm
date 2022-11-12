package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserRedPacket implements Serializable {

    private Long id;

    private Long acitivtyId;

    private Long userActivityId;

    private String name;

    private Byte type;

    private Long userId;

    private Long orderId;

    private BigDecimal amount;

    private Date expireTime;

    private Byte isShare;

    private Byte isValid;

    private Byte isUsed;

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

    public Long getAcitivtyId() {
        return acitivtyId;
    }

    public void setAcitivtyId(Long acitivtyId) {
        this.acitivtyId = acitivtyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Byte getIsShare() {
        return isShare;
    }

    public void setIsShare(Byte isShare) {
        this.isShare = isShare;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    public Byte getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Byte isUsed) {
        this.isUsed = isUsed;
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Long userActivityId) {
        this.userActivityId = userActivityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }
}