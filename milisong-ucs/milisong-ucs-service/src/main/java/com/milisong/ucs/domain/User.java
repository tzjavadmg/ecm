package com.milisong.ucs.domain;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 2514743956353253324L;
    private Long id;

    private String realName;

    private String mobileNo;

    private String nickName;

    private String headPortraitUrl;

    private Date birthday;

    private Integer sex;

    private String openId;

    private Byte isNew;

    private Byte businessLine;

    private Byte receivedNewRedPacket;

    private Byte receivedNewCoupon;

    private Integer registerSource;

    private String registerShopId;

    private String registerQrcode;

    private Date registerDate;

    private String wechatContractNo;

    private Integer usefulPoint;

    private Integer usedPoint;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl == null ? null : headPortraitUrl.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public Byte getIsNew() {
        return isNew;
    }

    public void setIsNew(Byte isNew) {
        this.isNew = isNew;
    }

    public Byte getReceivedNewRedPacket() {
        return receivedNewRedPacket;
    }

    public void setReceivedNewRedPacket(Byte receivedNewRedPacket) {
        this.receivedNewRedPacket = receivedNewRedPacket;
    }

    public Integer getRegisterSource() {
        return registerSource;
    }

    public void setRegisterSource(Integer registerSource) {
        this.registerSource = registerSource;
    }

    public String getRegisterShopId() {
        return registerShopId;
    }

    public void setRegisterShopId(String registerShopId) {
        this.registerShopId = registerShopId == null ? null : registerShopId.trim();
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getWechatContractNo() {
        return wechatContractNo;
    }

    public void setWechatContractNo(String wechatContractNo) {
        this.wechatContractNo = wechatContractNo == null ? null : wechatContractNo.trim();
    }

    public String getRegisterQrcode() {
        return registerQrcode;
    }

    public void setRegisterQrcode(String registerQrcode) {
        this.registerQrcode = registerQrcode;
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

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
    }

    public Byte getReceivedNewCoupon() {
        return receivedNewCoupon;
    }

    public void setReceivedNewCoupon(Byte receivedNewCoupon) {
        this.receivedNewCoupon = receivedNewCoupon;
    }

    public Integer getUsefulPoint() {
        return usefulPoint;
    }

    public void setUsefulPoint(Integer usefulPoint) {
        this.usefulPoint = usefulPoint;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public void setUsedPoint(Integer usedPoint) {
        this.usedPoint = usedPoint;
    }
}