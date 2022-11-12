/* https://github.com/orange1438 */
package com.milisong.dms.domain;

import java.util.Date;

public class ShunfengDeliveryLog {
    private Long id;

    private Byte type;

    private Byte deliveryStatus;

    private String deliveryUrl;

    private String param;

    private String result;

    private Date triggeringTime;

    private Byte businessType;

    private Byte status;

    private Date updateTime;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Byte deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryUrl() {
        return deliveryUrl;
    }

    public void setDeliveryUrl(String deliveryUrl) {
        this.deliveryUrl = deliveryUrl == null ? null : deliveryUrl.trim();
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public Date getTriggeringTime() {
        return triggeringTime;
    }

    public void setTriggeringTime(Date triggeringTime) {
        this.triggeringTime = triggeringTime;
    }

    public Byte getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Byte businessType) {
        this.businessType = businessType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}