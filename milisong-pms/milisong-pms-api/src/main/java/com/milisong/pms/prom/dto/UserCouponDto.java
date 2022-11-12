package com.milisong.pms.prom.dto;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2018/12/11 1:23 PM
 * @description
 */
public class UserCouponDto implements Serializable {

    private Long id;

    private Long activityId;

    private Long userActivityId;

    private String name;

    private Byte type;

    private Long userId;

    private Integer days;

    private String goodsCode;

    /**
     * 折扣率/x元购
     */
    private BigDecimal discount;

    private Date expireTime;

    private Byte isShare;

    private Byte isValid;

    private Byte isUsed;

    private Byte businessLine;

    private Boolean selected;

    private String expireTimeText;

    private String leftTimeText;

    private String mark;

    private String rule;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getExpireTimeText() {
        if (expireTime != null){
            return new DateTime(expireTime).toString("yyyy-MM-dd");
        }
        return expireTimeText;
    }

    public String getLeftTimeText() {
        if (expireTime != null){
            DateTime now = DateTime.now();
            DateTime exTime = new DateTime(expireTime);
            if (exTime.getDayOfYear() == now.getDayOfYear()){
                return "今日到期";
            }
        }
        return leftTimeText;
    }

    public String getMark() {
        if (days != null){
            return "单笔订满"+days+"天可用";
        }
        return mark;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Long getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Long userActivityId) {
        this.userActivityId = userActivityId;
    }
}