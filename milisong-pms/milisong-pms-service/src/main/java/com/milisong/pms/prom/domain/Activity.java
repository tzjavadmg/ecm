package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Activity implements Serializable {

    private Long id;

    private String name;

    private String descript;

    private Date startDate;

    private Date endDate;

    private Byte type;

    private Integer userLimit;

    private Integer activityLimit;

    private String dayOfWeek;

    private Byte isAllGoods;

    private String goodsCatalogIds;

    private String goodsIds;

    private String cityIds;

    private String buildingIds;

    private String companyIds;

    private BigDecimal minAmount;

    private Byte status;

    private Byte isDelete;

    private Byte businessLine;

    private Date createTime;

    private Date updateTime;

    private Byte isShare;

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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript == null ? null : descript.trim();
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

    public Integer getActivityLimit() {
        return activityLimit;
    }

    public void setActivityLimit(Integer activityLimit) {
        this.activityLimit = activityLimit;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek == null ? null : dayOfWeek.trim();
    }

    public Byte getIsAllGoods() {
        return isAllGoods;
    }

    public void setIsAllGoods(Byte isAllGoods) {
        this.isAllGoods = isAllGoods;
    }

    public String getGoodsCatalogIds() {
        return goodsCatalogIds;
    }

    public void setGoodsCatalogIds(String goodsCatalogIds) {
        this.goodsCatalogIds = goodsCatalogIds == null ? null : goodsCatalogIds.trim();
    }

    public String getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(String goodsIds) {
        this.goodsIds = goodsIds == null ? null : goodsIds.trim();
    }

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds == null ? null : cityIds.trim();
    }

    public String getBuildingIds() {
        return buildingIds;
    }

    public void setBuildingIds(String buildingIds) {
        this.buildingIds = buildingIds == null ? null : buildingIds.trim();
    }

    public String getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(String companyIds) {
        this.companyIds = companyIds == null ? null : companyIds.trim();
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Byte getIsShare() {
        return isShare;
    }

    public void setIsShare(Byte isShare) {
        this.isShare = isShare;
    }

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) &&
                Objects.equals(name, activity.name) &&
                Objects.equals(descript, activity.descript) &&
                Objects.equals(startDate, activity.startDate) &&
                Objects.equals(endDate, activity.endDate) &&
                Objects.equals(type, activity.type) &&
                Objects.equals(userLimit, activity.userLimit) &&
                Objects.equals(activityLimit, activity.activityLimit) &&
                Objects.equals(dayOfWeek, activity.dayOfWeek) &&
                Objects.equals(isAllGoods, activity.isAllGoods) &&
                Objects.equals(goodsCatalogIds, activity.goodsCatalogIds) &&
                Objects.equals(goodsIds, activity.goodsIds) &&
                Objects.equals(cityIds, activity.cityIds) &&
                Objects.equals(buildingIds, activity.buildingIds) &&
                Objects.equals(companyIds, activity.companyIds) &&
                Objects.equals(minAmount, activity.minAmount) &&
                Objects.equals(status, activity.status) &&
                Objects.equals(isDelete, activity.isDelete) &&
                Objects.equals(businessLine, activity.businessLine) &&
                Objects.equals(createTime, activity.createTime) &&
                Objects.equals(updateTime, activity.updateTime) &&
                Objects.equals(isShare, activity.isShare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, descript, startDate, endDate, type, userLimit, activityLimit, dayOfWeek, isAllGoods, goodsCatalogIds, goodsIds, cityIds, buildingIds, companyIds, minAmount, status, isDelete, businessLine, createTime, updateTime, isShare);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", descript='" + descript + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", type=" + type +
                ", userLimit=" + userLimit +
                ", activityLimit=" + activityLimit +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", isAllGoods=" + isAllGoods +
                ", goodsCatalogIds='" + goodsCatalogIds + '\'' +
                ", goodsIds='" + goodsIds + '\'' +
                ", cityIds='" + cityIds + '\'' +
                ", buildingIds='" + buildingIds + '\'' +
                ", companyIds='" + companyIds + '\'' +
                ", minAmount=" + minAmount +
                ", status=" + status +
                ", isDelete=" + isDelete +
                ", businessLine=" + businessLine +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isShare=" + isShare +
                '}';
    }
}