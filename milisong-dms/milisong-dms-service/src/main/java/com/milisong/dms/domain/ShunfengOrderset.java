/* https://github.com/orange1438 */
package com.milisong.dms.domain;

import java.util.Date;

public class ShunfengOrderset {
    private Long id;

    private Long detailSetId;

    private String shunfengOrderId;

    private Date credateTime;

    private Date updateTime;

    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDetailSetId() {
        return detailSetId;
    }

    public void setDetailSetId(Long detailSetId) {
        this.detailSetId = detailSetId;
    }

    public String getShunfengOrderId() {
        return shunfengOrderId;
    }

    public void setShunfengOrderId(String shunfengOrderId) {
        this.shunfengOrderId = shunfengOrderId == null ? null : shunfengOrderId.trim();
    }

    public Date getCredateTime() {
        return credateTime;
    }

    public void setCredateTime(Date credateTime) {
        this.credateTime = credateTime;
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