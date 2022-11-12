package com.milisong.pos.production.domain;

import java.math.BigDecimal;
import java.util.Date;

public class PreProduction {
    private Long id;

    private String goodsCode;

    private String goodsName;

    private Long shopId;

    private Date preProductionDate;

    private BigDecimal preProductionCount;

    private Integer actualProductionCount;

    private Integer actualSaleCount;

    private BigDecimal salesAverageCount;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;

    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getPreProductionDate() {
        return preProductionDate;
    }

    public void setPreProductionDate(Date preProductionDate) {
        this.preProductionDate = preProductionDate;
    }

    public BigDecimal getPreProductionCount() {
        return preProductionCount;
    }

    public void setPreProductionCount(BigDecimal preProductionCount) {
        this.preProductionCount = preProductionCount;
    }

    public Integer getActualProductionCount() {
        return actualProductionCount;
    }

    public void setActualProductionCount(Integer actualProductionCount) {
        this.actualProductionCount = actualProductionCount;
    }

    public Integer getActualSaleCount() {
        return actualSaleCount;
    }

    public void setActualSaleCount(Integer actualSaleCount) {
        this.actualSaleCount = actualSaleCount;
    }

    public BigDecimal getSalesAverageCount() {
        return salesAverageCount;
    }

    public void setSalesAverageCount(BigDecimal salesAverageCount) {
        this.salesAverageCount = salesAverageCount;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}