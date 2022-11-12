package com.milisong.pms.prom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class ActivityTimeSlot implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.deliveryId
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.activity_id
     *
     * @mbggenerated
     */
    private Long activityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.day_of_week
     *
     * @mbggenerated
     */
    private Byte dayOfWeek;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.start_time
     *
     * @mbggenerated
     */
    private Date startTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.end_time
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.discount_rate
     *
     * @mbggenerated
     */
    private BigDecimal discountRate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.is_delete
     *
     * @mbggenerated
     */
    private Byte isDelete;

    private Byte businessLine;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_time_slot.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_activity_time_slot
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.deliveryId
     *
     * @return the value of t_activity_time_slot.deliveryId
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.deliveryId
     *
     * @param id the value for t_activity_time_slot.deliveryId
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.activity_id
     *
     * @return the value of t_activity_time_slot.activity_id
     *
     * @mbggenerated
     */
    public Long getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.activity_id
     *
     * @param activityId the value for t_activity_time_slot.activity_id
     *
     * @mbggenerated
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.day_of_week
     *
     * @return the value of t_activity_time_slot.day_of_week
     *
     * @mbggenerated
     */
    public Byte getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.day_of_week
     *
     * @param dayOfWeek the value for t_activity_time_slot.day_of_week
     *
     * @mbggenerated
     */
    public void setDayOfWeek(Byte dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.start_time
     *
     * @return the value of t_activity_time_slot.start_time
     *
     * @mbggenerated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.start_time
     *
     * @param startTime the value for t_activity_time_slot.start_time
     *
     * @mbggenerated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.end_time
     *
     * @return the value of t_activity_time_slot.end_time
     *
     * @mbggenerated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.end_time
     *
     * @param endTime the value for t_activity_time_slot.end_time
     *
     * @mbggenerated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.discount_rate
     *
     * @return the value of t_activity_time_slot.discount_rate
     *
     * @mbggenerated
     */
    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.discount_rate
     *
     * @param discountRate the value for t_activity_time_slot.discount_rate
     *
     * @mbggenerated
     */
    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.is_delete
     *
     * @return the value of t_activity_time_slot.is_delete
     *
     * @mbggenerated
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.is_delete
     *
     * @param isDelete the value for t_activity_time_slot.is_delete
     *
     * @mbggenerated
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.create_time
     *
     * @return the value of t_activity_time_slot.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.create_time
     *
     * @param createTime the value for t_activity_time_slot.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_time_slot.update_time
     *
     * @return the value of t_activity_time_slot.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_time_slot.update_time
     *
     * @param updateTime the value for t_activity_time_slot.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityTimeSlot that = (ActivityTimeSlot) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(activityId, that.activityId) &&
                Objects.equals(dayOfWeek, that.dayOfWeek) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(discountRate, that.discountRate) &&
                Objects.equals(isDelete, that.isDelete) &&
                Objects.equals(businessLine, that.businessLine) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activityId, dayOfWeek, startTime, endTime, discountRate, isDelete, businessLine, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "ActivityTimeSlot{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", discountRate=" + discountRate +
                ", isDelete=" + isDelete +
                ", businessLine=" + businessLine +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}