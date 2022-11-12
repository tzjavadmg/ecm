package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ActivityRedPacket implements Serializable {

    private Long id;

    private Long activityId;

    private Byte type;

    private BigDecimal amount;

    private Byte players;

    private Byte valid;

    private Byte clickLimit;

    private Byte businessLine;

    private Byte isDelete;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Byte getClickLimit() {
        return clickLimit;
    }

    public void setClickLimit(Byte clickLimit) {
        this.clickLimit = clickLimit;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
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

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }


}