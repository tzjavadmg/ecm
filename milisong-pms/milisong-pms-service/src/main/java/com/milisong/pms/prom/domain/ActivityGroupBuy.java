package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ActivityGroupBuy implements Serializable {
    private Long id;

    private String name;

    private String goodsCode;

    private BigDecimal buyPrice;

    private Integer minJoinNum;

    private Date startDate;

    private Date endDate;

    private Integer validPeriod;

    private Date deliveryDate;

    private String bannerImg;

    private String bgImg;

    private String ruleImg;

    private String shareTitle;

    private String shareImg;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getMinJoinNum() {
        return minJoinNum;
    }

    public void setMinJoinNum(Integer minJoinNum) {
        this.minJoinNum = minJoinNum;
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

    public Integer getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(Integer validPeriod) {
        this.validPeriod = validPeriod;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg == null ? null : bannerImg.trim();
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg == null ? null : bgImg.trim();
    }

    public String getRuleImg() {
        return ruleImg;
    }

    public void setRuleImg(String ruleImg) {
        this.ruleImg = ruleImg == null ? null : ruleImg.trim();
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle == null ? null : shareTitle.trim();
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg == null ? null : shareImg.trim();
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
        ActivityGroupBuy other = (ActivityGroupBuy) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getGoodsCode() == null ? other.getGoodsCode() == null : this.getGoodsCode().equals(other.getGoodsCode()))
                && (this.getBuyPrice() == null ? other.getBuyPrice() == null : this.getBuyPrice().equals(other.getBuyPrice()))
                && (this.getMinJoinNum() == null ? other.getMinJoinNum() == null : this.getMinJoinNum().equals(other.getMinJoinNum()))
                && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
                && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
                && (this.getValidPeriod() == null ? other.getValidPeriod() == null : this.getValidPeriod().equals(other.getValidPeriod()))
                && (this.getDeliveryDate() == null ? other.getDeliveryDate() == null : this.getDeliveryDate().equals(other.getDeliveryDate()))
                && (this.getBannerImg() == null ? other.getBannerImg() == null : this.getBannerImg().equals(other.getBannerImg()))
                && (this.getBgImg() == null ? other.getBgImg() == null : this.getBgImg().equals(other.getBgImg()))
                && (this.getRuleImg() == null ? other.getRuleImg() == null : this.getRuleImg().equals(other.getRuleImg()))
                && (this.getShareTitle() == null ? other.getShareTitle() == null : this.getShareTitle().equals(other.getShareTitle()))
                && (this.getShareImg() == null ? other.getShareImg() == null : this.getShareImg().equals(other.getShareImg()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getGoodsCode() == null) ? 0 : getGoodsCode().hashCode());
        result = prime * result + ((getBuyPrice() == null) ? 0 : getBuyPrice().hashCode());
        result = prime * result + ((getMinJoinNum() == null) ? 0 : getMinJoinNum().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getValidPeriod() == null) ? 0 : getValidPeriod().hashCode());
        result = prime * result + ((getDeliveryDate() == null) ? 0 : getDeliveryDate().hashCode());
        result = prime * result + ((getBannerImg() == null) ? 0 : getBannerImg().hashCode());
        result = prime * result + ((getBgImg() == null) ? 0 : getBgImg().hashCode());
        result = prime * result + ((getRuleImg() == null) ? 0 : getRuleImg().hashCode());
        result = prime * result + ((getShareTitle() == null) ? 0 : getShareTitle().hashCode());
        result = prime * result + ((getShareImg() == null) ? 0 : getShareImg().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", goodsCode=").append(goodsCode);
        sb.append(", buyPrice=").append(buyPrice);
        sb.append(", minJoinNum=").append(minJoinNum);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", validPeriod=").append(validPeriod);
        sb.append(", deliveryDate=").append(deliveryDate);
        sb.append(", bannerImg=").append(bannerImg);
        sb.append(", bgImg=").append(bgImg);
        sb.append(", ruleImg=").append(ruleImg);
        sb.append(", shareTitle=").append(shareTitle);
        sb.append(", shareImg=").append(shareImg);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}