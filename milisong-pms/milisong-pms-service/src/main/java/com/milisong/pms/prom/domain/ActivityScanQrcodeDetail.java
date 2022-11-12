package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.util.Date;

public class ActivityScanQrcodeDetail implements Serializable {
    private Long id;

    private Long actScanQrcodeId;

    private String qrcode;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActScanQrcodeId() {
        return actScanQrcodeId;
    }

    public void setActScanQrcodeId(Long actScanQrcodeId) {
        this.actScanQrcodeId = actScanQrcodeId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode == null ? null : qrcode.trim();
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
        ActivityScanQrcodeDetail other = (ActivityScanQrcodeDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getActScanQrcodeId() == null ? other.getActScanQrcodeId() == null : this.getActScanQrcodeId().equals(other.getActScanQrcodeId()))
            && (this.getQrcode() == null ? other.getQrcode() == null : this.getQrcode().equals(other.getQrcode()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getActScanQrcodeId() == null) ? 0 : getActScanQrcodeId().hashCode());
        result = prime * result + ((getQrcode() == null) ? 0 : getQrcode().hashCode());
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
        sb.append(", actScanQrcodeId=").append(actScanQrcodeId);
        sb.append(", qrcode=").append(qrcode);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}