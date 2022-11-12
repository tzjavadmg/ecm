package com.milisong.ecm.common.user.dto;

import java.io.Serializable;

public class UserFeedbackDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -5776091264133160848L;

    private Long id;

    private Long userId;

    private Long feedbackType;

    private String nickName;

    private String mobileNo;

    private Long orderId;

    private String orderNo;

    private String feedbackOpinion;

    private Integer grade;

    private Boolean transit;

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

    public Long getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Long feedbackType) {
        this.feedbackType = feedbackType;
    }

    public Boolean getTransit() {
        return transit;
    }

    public void setTransit(Boolean transit) {
        this.transit = transit;
    }
}