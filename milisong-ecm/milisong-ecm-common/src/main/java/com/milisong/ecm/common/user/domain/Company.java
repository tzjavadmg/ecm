package com.milisong.ecm.common.user.domain;

import java.util.Date;

public class Company {
    private Long id;

    private String name;

    private String abbreviation;

    private Long buildingId;

    private String buildingAbbreviation;

    private String floor;

    private String roomNumber;

    private Integer employeesNumber;

    private Boolean isCertification;

    private Boolean isSameName;

    private String sameName;

    private Boolean isDeleted;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation == null ? null : abbreviation.trim();
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingAbbreviation() {
        return buildingAbbreviation;
    }

    public void setBuildingAbbreviation(String buildingAbbreviation) {
        this.buildingAbbreviation = buildingAbbreviation == null ? null : buildingAbbreviation.trim();
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }

    public Integer getEmployeesNumber() {
        return employeesNumber;
    }

    public void setEmployeesNumber(Integer employeesNumber) {
        this.employeesNumber = employeesNumber;
    }

    public Boolean getIsCertification() {
        return isCertification;
    }

    public void setIsCertification(Boolean isCertification) {
        this.isCertification = isCertification;
    }

    public Boolean getIsSameName() {
        return isSameName;
    }

    public void setIsSameName(Boolean isSameName) {
        this.isSameName = isSameName;
    }

    public String getSameName() {
        return sameName;
    }

    public void setSameName(String sameName) {
        this.sameName = sameName == null ? null : sameName.trim();
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}