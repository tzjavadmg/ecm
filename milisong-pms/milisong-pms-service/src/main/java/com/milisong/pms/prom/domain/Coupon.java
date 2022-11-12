package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Coupon implements Serializable {
    /**
     * ID
     */
    private Long id;

    /**
     * 券名称
     */
    private String name;

    /**
     * 券类型：1 折扣券 2 商品券
     */
    private Byte type;

    /**
     *  针对type = 2，0 商品券 1商品套餐券
     */
    private Byte label;

    /**
     * 门槛天数(满x天)
     */
    private Integer limitDays;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 折扣/优惠金额
     */
    private BigDecimal discount;

    /**
     * 是否与其它活动共享，0 不同享 1 同享
     */
    private Byte isShare;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 券使用规则
     */
    private String rule;

    /**
     * 业务线：0 早餐、1  午餐、2 下午茶、3 晚餐、4 夜宵
     */
    private Byte businessLine;

    /**
     * 发布状态：0 下线、1 上线
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getLimitDays() {
        return limitDays;
    }

    public void setLimitDays(Integer limitDays) {
        this.limitDays = limitDays;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Byte getIsShare() {
        return isShare;
    }

    public void setIsShare(Byte isShare) {
        this.isShare = isShare;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public Byte getLabel() {
        return label;
    }

    public void setLabel(Byte label) {
        this.label = label;
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
        Coupon other = (Coupon) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getLimitDays() == null ? other.getLimitDays() == null : this.getLimitDays().equals(other.getLimitDays()))
            && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
            && (this.getGoodsCode() == null ? other.getGoodsCode() == null : this.getGoodsCode().equals(other.getGoodsCode()))
            && (this.getDiscount() == null ? other.getDiscount() == null : this.getDiscount().equals(other.getDiscount()))
            && (this.getIsShare() == null ? other.getIsShare() == null : this.getIsShare().equals(other.getIsShare()))
            && (this.getValidDays() == null ? other.getValidDays() == null : this.getValidDays().equals(other.getValidDays()))
            && (this.getRule() == null ? other.getRule() == null : this.getRule().equals(other.getRule()))
            && (this.getBusinessLine() == null ? other.getBusinessLine() == null : this.getBusinessLine().equals(other.getBusinessLine()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getLabel() == null ? other.getLabel() == null : this.getLabel().equals(other.getLabel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getLimitDays() == null) ? 0 : getLimitDays().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getGoodsCode() == null) ? 0 : getGoodsCode().hashCode());
        result = prime * result + ((getDiscount() == null) ? 0 : getDiscount().hashCode());
        result = prime * result + ((getIsShare() == null) ? 0 : getIsShare().hashCode());
        result = prime * result + ((getValidDays() == null) ? 0 : getValidDays().hashCode());
        result = prime * result + ((getRule() == null) ? 0 : getRule().hashCode());
        result = prime * result + ((getBusinessLine() == null) ? 0 : getBusinessLine().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", limitDays=").append(limitDays);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", goodsCode=").append(goodsCode);
        sb.append(", discount=").append(discount);
        sb.append(", isShare=").append(isShare);
        sb.append(", validDays=").append(validDays);
        sb.append(", rule=").append(rule);
        sb.append(", businessLine=").append(businessLine);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isPackage=").append(label);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}