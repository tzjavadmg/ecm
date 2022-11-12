package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.util.Date;

public class SendRedpacketRecord implements Serializable {
    private Long id;

    private Long operatorId;

    private String operatorName;

    private String filterCondition;

    private String redpacketids;

    private String content;

    private Date sendTime;

    private Integer shouldSendNum;

    private Integer actualSendNum;

    private String remark;

    private Byte status;

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

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition == null ? null : filterCondition.trim();
    }

    public String getRedpacketids() {
        return redpacketids;
    }

    public void setRedpacketids(String redpacketids) {
        this.redpacketids = redpacketids == null ? null : redpacketids.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getShouldSendNum() {
        return shouldSendNum;
    }

    public void setShouldSendNum(Integer shouldSendNum) {
        this.shouldSendNum = shouldSendNum;
    }

    public Integer getActualSendNum() {
        return actualSendNum;
    }

    public void setActualSendNum(Integer actualSendNum) {
        this.actualSendNum = actualSendNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
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
        SendRedpacketRecord other = (SendRedpacketRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getOperatorName() == null ? other.getOperatorName() == null : this.getOperatorName().equals(other.getOperatorName()))
            && (this.getFilterCondition() == null ? other.getFilterCondition() == null : this.getFilterCondition().equals(other.getFilterCondition()))
            && (this.getRedpacketids() == null ? other.getRedpacketids() == null : this.getRedpacketids().equals(other.getRedpacketids()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
            && (this.getShouldSendNum() == null ? other.getShouldSendNum() == null : this.getShouldSendNum().equals(other.getShouldSendNum()))
            && (this.getActualSendNum() == null ? other.getActualSendNum() == null : this.getActualSendNum().equals(other.getActualSendNum()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBusinessLine() == null ? other.getBusinessLine() == null : this.getBusinessLine().equals(other.getBusinessLine()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getOperatorName() == null) ? 0 : getOperatorName().hashCode());
        result = prime * result + ((getFilterCondition() == null) ? 0 : getFilterCondition().hashCode());
        result = prime * result + ((getRedpacketids() == null) ? 0 : getRedpacketids().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getShouldSendNum() == null) ? 0 : getShouldSendNum().hashCode());
        result = prime * result + ((getActualSendNum() == null) ? 0 : getActualSendNum().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBusinessLine() == null) ? 0 : getBusinessLine().hashCode());
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
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", filterCondition=").append(filterCondition);
        sb.append(", redpacketids=").append(redpacketids);
        sb.append(", content=").append(content);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", shouldSendNum=").append(shouldSendNum);
        sb.append(", actualSendNum=").append(actualSendNum);
        sb.append(", remark=").append(remark);
        sb.append(", status=").append(status);
        sb.append(", businessLine=").append(businessLine);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}