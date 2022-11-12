package com.milisong.ecm.common.user.domain;

import java.io.Serializable;
import java.util.Date;

public class UserFeedback implements Serializable {

    private Long id;

    private Long userId;

    private String nickName;

    private String mobileNo;

    private Long orderId;

    private String orderNo;

    private Long feedbackType;

    private String feedbackOpinion;

    private Integer grade;

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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Long feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackOpinion() {
        return feedbackOpinion;
    }

    public void setFeedbackOpinion(String feedbackOpinion) {
        this.feedbackOpinion = feedbackOpinion == null ? null : feedbackOpinion.trim();
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
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

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }
}