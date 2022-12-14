package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ActivityRedPacketAction implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.deliveryId
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.user_activity_id
     *
     * @mbggenerated
     */
    private Long userActivityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.user_id
     *
     * @mbggenerated
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.open_id
     *
     * @mbggenerated
     */
    private String openId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.nick_name
     *
     * @mbggenerated
     */
    private String nickName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.head_portrait_url
     *
     * @mbggenerated
     */
    private String headPortraitUrl;

    private Byte businessLine;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_red_packet_action.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_activity_red_packet_action
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.deliveryId
     *
     * @return the value of t_activity_red_packet_action.deliveryId
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.deliveryId
     *
     * @param id the value for t_activity_red_packet_action.deliveryId
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.user_activity_id
     *
     * @return the value of t_activity_red_packet_action.user_activity_id
     *
     * @mbggenerated
     */
    public Long getUserActivityId() {
        return userActivityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.user_activity_id
     *
     * @param userActivityId the value for t_activity_red_packet_action.user_activity_id
     *
     * @mbggenerated
     */
    public void setUserActivityId(Long userActivityId) {
        this.userActivityId = userActivityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.user_id
     *
     * @return the value of t_activity_red_packet_action.user_id
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.user_id
     *
     * @param userId the value for t_activity_red_packet_action.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.open_id
     *
     * @return the value of t_activity_red_packet_action.open_id
     *
     * @mbggenerated
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.open_id
     *
     * @param openId the value for t_activity_red_packet_action.open_id
     *
     * @mbggenerated
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.nick_name
     *
     * @return the value of t_activity_red_packet_action.nick_name
     *
     * @mbggenerated
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.nick_name
     *
     * @param nickName the value for t_activity_red_packet_action.nick_name
     *
     * @mbggenerated
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.head_portrait_url
     *
     * @return the value of t_activity_red_packet_action.head_portrait_url
     *
     * @mbggenerated
     */
    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.head_portrait_url
     *
     * @param headPortraitUrl the value for t_activity_red_packet_action.head_portrait_url
     *
     * @mbggenerated
     */
    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl == null ? null : headPortraitUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.create_time
     *
     * @return the value of t_activity_red_packet_action.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.create_time
     *
     * @param createTime the value for t_activity_red_packet_action.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_red_packet_action.update_time
     *
     * @return the value of t_activity_red_packet_action.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_red_packet_action.update_time
     *
     * @param updateTime the value for t_activity_red_packet_action.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        ActivityRedPacketAction that = (ActivityRedPacketAction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userActivityId, that.userActivityId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(openId, that.openId) &&
                Objects.equals(nickName, that.nickName) &&
                Objects.equals(headPortraitUrl, that.headPortraitUrl) &&
                Objects.equals(businessLine, that.businessLine) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userActivityId, userId, openId, nickName, headPortraitUrl, businessLine, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "ActivityRedPacketAction{" +
                "id=" + id +
                ", userActivityId=" + userActivityId +
                ", userId=" + userId +
                ", openId='" + openId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headPortraitUrl='" + headPortraitUrl + '\'' +
                ", businessLine=" + businessLine +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}