package com.milisong.ucs.sdk.security;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserSession {
    private Long id;

    private String realName;

    private String mobileNo;

    private String nickName;

    private String headPortraitUrl;

    private Date birthday;

    private Integer sex;

    private String openId;

    private Byte isNew;

    private Byte receivedNewRedPacket;

    private Byte receivedNewCoupon;

    private Byte businessLine;

    private Integer registerSource;

    private String registerShopId;

    private Date registerDate;

    private String wechatContractNo;

    private Integer usefulPoint;

    private Integer usedPoint;

    //会话密钥
    private String wxSessionKey;

    //地址id
    private Long addressId;

    //楼宇id
    private Long deliveryOfficeBuildingId;

    //楼层
    private String deliveryFloor;

    //公司名称
    private String deliveryCompany;

    //通知类型
    private Integer notifyType;


    public Map<String, Object> toSessionMap() {
        Map<String, Object> sessionMap = new HashMap<>();
        BeanMapper.copy(this, sessionMap);
        sessionMap.put("id", id.toString());
        sessionMap.put("sex", sex == null ? "" : sex.toString());
        sessionMap.put("isNew", isNew == null ? "" : isNew.toString());
        sessionMap.put("receivedNewRedPacket", receivedNewRedPacket == null ? "" : receivedNewRedPacket.toString());
        sessionMap.put("receivedNewCoupon", receivedNewCoupon == null ? "" : receivedNewCoupon.toString());
        sessionMap.put("businessLine", businessLine == null ? "1" : businessLine.toString());
        sessionMap.put("addressId", addressId == null ? "" : addressId.toString());
        sessionMap.put("notifyType", notifyType == null ? "" : notifyType.toString());
        sessionMap.put("deliveryOfficeBuildingId", deliveryOfficeBuildingId == null ? "" : deliveryOfficeBuildingId.toString());
        sessionMap.put("registerSource", registerSource == null ? "" : registerSource.toString());
        sessionMap.put("usefulPoint", usefulPoint == null ? "0" : usefulPoint.toString());
        sessionMap.put("usedPoint", usedPoint == null ? "0" : usedPoint.toString());
        if (registerDate != null) {
            sessionMap.put("registerDate", DateFormatUtils.format(registerDate, "yyyy-MM-dd HH:mm"));
        }
        if (birthday != null) {
            sessionMap.put("birthday", DateFormatUtils.format(birthday, "yyyy-MM-dd HH:mm"));
        }
        return sessionMap;
    }

    public static UserSession toUserSession(Map<Object, Object> sessionMap) {
        log.info("sessionMap -> {}", JSON.toJSONString(sessionMap));
        UserSession session = new UserSession();
        Long id = MapUtils.getLong(sessionMap, "id");
        Integer sex = MapUtils.getInteger(sessionMap, "sex");
        Byte isNew = MapUtils.getByte(sessionMap, "isNew");
        Byte receivedNewRedPacket = MapUtils.getByte(sessionMap, "receivedNewRedPacket");
        Byte receivedNewCoupon = MapUtils.getByte(sessionMap, "receivedNewCoupon");
        Byte businessLine = MapUtils.getByte(sessionMap, "businessLine");
        Integer registerSource = MapUtils.getInteger(sessionMap, "registerSource");
        Integer usefulPoint = MapUtils.getInteger(sessionMap, "usefulPoint");
        Integer usedPoint = MapUtils.getInteger(sessionMap, "usedPoint");
        String registerDateStr = MapUtils.getString(sessionMap, "registerDate");
        String birthdayStr = MapUtils.getString(sessionMap, "birthday");
        Date registerDate = null;
        Date birthday = null;
        try {
            if (StringUtils.isNotEmpty(registerDateStr)) {
                registerDate = DateUtils.parseDate(registerDateStr, "yyyy-MM-dd HH:mm");
            }
        } catch (ParseException e) {
            log.error("", e);
        }
        try {
            if (StringUtils.isNotEmpty(birthdayStr)) {
                birthday = DateUtils.parseDate(birthdayStr, "yyyy-MM-dd HH:mm");
            }
        } catch (ParseException e) {
            log.error("", e);
        }
        session.setId(id);
        session.setSex(sex);
        session.setIsNew(isNew);
        session.setReceivedNewRedPacket(receivedNewRedPacket);
        session.setReceivedNewCoupon(receivedNewCoupon);
        session.setBusinessLine(businessLine);
        session.setRegisterSource(registerSource);
        session.setUsefulPoint(usefulPoint);
        session.setUsedPoint(usedPoint);
        session.setBirthday(birthday);
        session.setRegisterDate(registerDate);
        session.setWxSessionKey(MapUtils.getString(sessionMap, "wxSessionKey"));
        session.setRealName(MapUtils.getString(sessionMap, "realName"));
        session.setMobileNo(MapUtils.getString(sessionMap, "mobileNo"));
        session.setNickName(MapUtils.getString(sessionMap, "nickName"));
        session.setHeadPortraitUrl(MapUtils.getString(sessionMap, "headPortraitUrl"));
        session.setOpenId(MapUtils.getString(sessionMap, "openId"));
        session.setRegisterShopId(MapUtils.getString(sessionMap, "registerShopId"));
        session.setWechatContractNo(MapUtils.getString(sessionMap, "wechatContractNo"));
        session.setAddressId(MapUtils.getLong(sessionMap, "addressId")); //地址id
        session.setDeliveryOfficeBuildingId(MapUtils.getLong(sessionMap, "deliveryOfficeBuildingId"));//楼宇id
        session.setDeliveryFloor(MapUtils.getString(sessionMap, "deliveryFloor"));//楼层
        session.setDeliveryCompany(MapUtils.getString(sessionMap, "deliveryCompany"));//公司
        session.setNotifyType(MapUtils.getInteger(sessionMap, "notifyType"));
        log.info("session -> {}", JSON.toJSONString(session));
        return session;
    }

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

    public Byte getReceivedNewRedPacket() {
        return receivedNewRedPacket;
    }

    public void setReceivedNewRedPacket(Byte receivedNewRedPacket) {
        this.receivedNewRedPacket = receivedNewRedPacket;
    }

    public Byte getReceivedNewCoupon() {
        return receivedNewCoupon;
    }

    public void setReceivedNewCoupon(Byte receivedNewCoupon) {
        this.receivedNewCoupon = receivedNewCoupon;
    }

    public Byte getIsNew() {
        return isNew;
    }

    public void setIsNew(Byte isNew) {
        this.isNew = isNew;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
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

    public String getWxSessionKey() {
        return wxSessionKey;
    }

    public void setWxSessionKey(String wxSessionKey) {
        this.wxSessionKey = wxSessionKey;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getDeliveryOfficeBuildingId() {
        return deliveryOfficeBuildingId;
    }

    public void setDeliveryOfficeBuildingId(Long deliveryOfficeBuildingId) {
        this.deliveryOfficeBuildingId = deliveryOfficeBuildingId;
    }

    public String getDeliveryFloor() {
        return deliveryFloor;
    }

    public void setDeliveryFloor(String deliveryFloor) {
        this.deliveryFloor = deliveryFloor;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public Integer getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }

    public Byte getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Byte businessLine) {
        this.businessLine = businessLine;
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