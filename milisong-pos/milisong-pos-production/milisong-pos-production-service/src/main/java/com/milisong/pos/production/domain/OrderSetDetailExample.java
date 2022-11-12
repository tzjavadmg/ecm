package com.milisong.pos.production.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class OrderSetDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderSetDetailExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                timeList.add(new java.sql.Time(iter.next().getTime()));
            }
            addCriterion(condition, timeList, property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSetNoIsNull() {
            addCriterion("set_no is null");
            return (Criteria) this;
        }

        public Criteria andSetNoIsNotNull() {
            addCriterion("set_no is not null");
            return (Criteria) this;
        }

        public Criteria andSetNoEqualTo(String value) {
            addCriterion("set_no =", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoNotEqualTo(String value) {
            addCriterion("set_no <>", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoGreaterThan(String value) {
            addCriterion("set_no >", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoGreaterThanOrEqualTo(String value) {
            addCriterion("set_no >=", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoLessThan(String value) {
            addCriterion("set_no <", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoLessThanOrEqualTo(String value) {
            addCriterion("set_no <=", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoLike(String value) {
            addCriterion("set_no like", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoNotLike(String value) {
            addCriterion("set_no not like", value, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoIn(List<String> values) {
            addCriterion("set_no in", values, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoNotIn(List<String> values) {
            addCriterion("set_no not in", values, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoBetween(String value1, String value2) {
            addCriterion("set_no between", value1, value2, "setNo");
            return (Criteria) this;
        }

        public Criteria andSetNoNotBetween(String value1, String value2) {
            addCriterion("set_no not between", value1, value2, "setNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoIsNull() {
            addCriterion("detail_set_no is null");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoIsNotNull() {
            addCriterion("detail_set_no is not null");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoEqualTo(String value) {
            addCriterion("detail_set_no =", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoNotEqualTo(String value) {
            addCriterion("detail_set_no <>", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoGreaterThan(String value) {
            addCriterion("detail_set_no >", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoGreaterThanOrEqualTo(String value) {
            addCriterion("detail_set_no >=", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoLessThan(String value) {
            addCriterion("detail_set_no <", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoLessThanOrEqualTo(String value) {
            addCriterion("detail_set_no <=", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoLike(String value) {
            addCriterion("detail_set_no like", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoNotLike(String value) {
            addCriterion("detail_set_no not like", value, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoIn(List<String> values) {
            addCriterion("detail_set_no in", values, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoNotIn(List<String> values) {
            addCriterion("detail_set_no not in", values, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoBetween(String value1, String value2) {
            addCriterion("detail_set_no between", value1, value2, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoNotBetween(String value1, String value2) {
            addCriterion("detail_set_no not between", value1, value2, "detailSetNo");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionIsNull() {
            addCriterion("detail_set_no_description is null");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionIsNotNull() {
            addCriterion("detail_set_no_description is not null");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionEqualTo(String value) {
            addCriterion("detail_set_no_description =", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotEqualTo(String value) {
            addCriterion("detail_set_no_description <>", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionGreaterThan(String value) {
            addCriterion("detail_set_no_description >", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("detail_set_no_description >=", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionLessThan(String value) {
            addCriterion("detail_set_no_description <", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionLessThanOrEqualTo(String value) {
            addCriterion("detail_set_no_description <=", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionLike(String value) {
            addCriterion("detail_set_no_description like", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotLike(String value) {
            addCriterion("detail_set_no_description not like", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionIn(List<String> values) {
            addCriterion("detail_set_no_description in", values, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotIn(List<String> values) {
            addCriterion("detail_set_no_description not in", values, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionBetween(String value1, String value2) {
            addCriterion("detail_set_no_description between", value1, value2, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotBetween(String value1, String value2) {
            addCriterion("detail_set_no_description not between", value1, value2, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateIsNull() {
            addCriterion("detail_delivery_date is null");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateIsNotNull() {
            addCriterion("detail_delivery_date is not null");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateEqualTo(Date value) {
            addCriterionForJDBCDate("detail_delivery_date =", value, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("detail_delivery_date <>", value, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateGreaterThan(Date value) {
            addCriterionForJDBCDate("detail_delivery_date >", value, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("detail_delivery_date >=", value, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateLessThan(Date value) {
            addCriterionForJDBCDate("detail_delivery_date <", value, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("detail_delivery_date <=", value, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateIn(List<Date> values) {
            addCriterionForJDBCDate("detail_delivery_date in", values, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("detail_delivery_date not in", values, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("detail_delivery_date between", value1, value2, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("detail_delivery_date not between", value1, value2, "detailDeliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeIsNull() {
            addCriterion("delivery_start_time is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeIsNotNull() {
            addCriterion("delivery_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_start_time =", value, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_start_time <>", value, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("delivery_start_time >", value, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_start_time >=", value, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeLessThan(Date value) {
            addCriterionForJDBCTime("delivery_start_time <", value, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_start_time <=", value, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_start_time in", values, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_start_time not in", values, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_start_time between", value1, value2, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_start_time not between", value1, value2, "deliveryStartTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeIsNull() {
            addCriterion("delivery_end_time is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeIsNotNull() {
            addCriterion("delivery_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_end_time =", value, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_end_time <>", value, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("delivery_end_time >", value, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_end_time >=", value, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeLessThan(Date value) {
            addCriterionForJDBCTime("delivery_end_time <", value, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_end_time <=", value, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_end_time in", values, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_end_time not in", values, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_end_time between", value1, value2, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_end_time not between", value1, value2, "deliveryEndTime");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressIsNull() {
            addCriterion("detail_delivery_address is null");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressIsNotNull() {
            addCriterion("detail_delivery_address is not null");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressEqualTo(String value) {
            addCriterion("detail_delivery_address =", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressNotEqualTo(String value) {
            addCriterion("detail_delivery_address <>", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressGreaterThan(String value) {
            addCriterion("detail_delivery_address >", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressGreaterThanOrEqualTo(String value) {
            addCriterion("detail_delivery_address >=", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressLessThan(String value) {
            addCriterion("detail_delivery_address <", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressLessThanOrEqualTo(String value) {
            addCriterion("detail_delivery_address <=", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressLike(String value) {
            addCriterion("detail_delivery_address like", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressNotLike(String value) {
            addCriterion("detail_delivery_address not like", value, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressIn(List<String> values) {
            addCriterion("detail_delivery_address in", values, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressNotIn(List<String> values) {
            addCriterion("detail_delivery_address not in", values, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressBetween(String value1, String value2) {
            addCriterion("detail_delivery_address between", value1, value2, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDetailDeliveryAddressNotBetween(String value1, String value2) {
            addCriterion("detail_delivery_address not between", value1, value2, "detailDeliveryAddress");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNull() {
            addCriterion("shop_id is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("shop_id is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(Long value) {
            addCriterion("shop_id =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Long value) {
            addCriterion("shop_id <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Long value) {
            addCriterion("shop_id >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Long value) {
            addCriterion("shop_id >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Long value) {
            addCriterion("shop_id <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Long value) {
            addCriterion("shop_id <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Long> values) {
            addCriterion("shop_id in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Long> values) {
            addCriterion("shop_id not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Long value1, Long value2) {
            addCriterion("shop_id between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Long value1, Long value2) {
            addCriterion("shop_id not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdIsNull() {
            addCriterion("building_id is null");
            return (Criteria) this;
        }

        public Criteria andBuildingIdIsNotNull() {
            addCriterion("building_id is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingIdEqualTo(Long value) {
            addCriterion("building_id =", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotEqualTo(Long value) {
            addCriterion("building_id <>", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdGreaterThan(Long value) {
            addCriterion("building_id >", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("building_id >=", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdLessThan(Long value) {
            addCriterion("building_id <", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdLessThanOrEqualTo(Long value) {
            addCriterion("building_id <=", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdIn(List<Long> values) {
            addCriterion("building_id in", values, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotIn(List<Long> values) {
            addCriterion("building_id not in", values, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdBetween(Long value1, Long value2) {
            addCriterion("building_id between", value1, value2, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotBetween(Long value1, Long value2) {
            addCriterion("building_id not between", value1, value2, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationIsNull() {
            addCriterion("building_abbreviation is null");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationIsNotNull() {
            addCriterion("building_abbreviation is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationEqualTo(String value) {
            addCriterion("building_abbreviation =", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotEqualTo(String value) {
            addCriterion("building_abbreviation <>", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationGreaterThan(String value) {
            addCriterion("building_abbreviation >", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationGreaterThanOrEqualTo(String value) {
            addCriterion("building_abbreviation >=", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationLessThan(String value) {
            addCriterion("building_abbreviation <", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationLessThanOrEqualTo(String value) {
            addCriterion("building_abbreviation <=", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationLike(String value) {
            addCriterion("building_abbreviation like", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotLike(String value) {
            addCriterion("building_abbreviation not like", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationIn(List<String> values) {
            addCriterion("building_abbreviation in", values, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotIn(List<String> values) {
            addCriterion("building_abbreviation not in", values, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationBetween(String value1, String value2) {
            addCriterion("building_abbreviation between", value1, value2, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotBetween(String value1, String value2) {
            addCriterion("building_abbreviation not between", value1, value2, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIsNull() {
            addCriterion("delivery_floor is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIsNotNull() {
            addCriterion("delivery_floor is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorEqualTo(String value) {
            addCriterion("delivery_floor =", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotEqualTo(String value) {
            addCriterion("delivery_floor <>", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorGreaterThan(String value) {
            addCriterion("delivery_floor >", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorGreaterThanOrEqualTo(String value) {
            addCriterion("delivery_floor >=", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLessThan(String value) {
            addCriterion("delivery_floor <", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLessThanOrEqualTo(String value) {
            addCriterion("delivery_floor <=", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLike(String value) {
            addCriterion("delivery_floor like", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotLike(String value) {
            addCriterion("delivery_floor not like", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIn(List<String> values) {
            addCriterion("delivery_floor in", values, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotIn(List<String> values) {
            addCriterion("delivery_floor not in", values, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorBetween(String value1, String value2) {
            addCriterion("delivery_floor between", value1, value2, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotBetween(String value1, String value2) {
            addCriterion("delivery_floor not between", value1, value2, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andMealAddressIsNull() {
            addCriterion("meal_address is null");
            return (Criteria) this;
        }

        public Criteria andMealAddressIsNotNull() {
            addCriterion("meal_address is not null");
            return (Criteria) this;
        }

        public Criteria andMealAddressEqualTo(String value) {
            addCriterion("meal_address =", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressNotEqualTo(String value) {
            addCriterion("meal_address <>", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressGreaterThan(String value) {
            addCriterion("meal_address >", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressGreaterThanOrEqualTo(String value) {
            addCriterion("meal_address >=", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressLessThan(String value) {
            addCriterion("meal_address <", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressLessThanOrEqualTo(String value) {
            addCriterion("meal_address <=", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressLike(String value) {
            addCriterion("meal_address like", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressNotLike(String value) {
            addCriterion("meal_address not like", value, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressIn(List<String> values) {
            addCriterion("meal_address in", values, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressNotIn(List<String> values) {
            addCriterion("meal_address not in", values, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressBetween(String value1, String value2) {
            addCriterion("meal_address between", value1, value2, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andMealAddressNotBetween(String value1, String value2) {
            addCriterion("meal_address not between", value1, value2, "mealAddress");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNull() {
            addCriterion("company_id is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNotNull() {
            addCriterion("company_id is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdEqualTo(Long value) {
            addCriterion("company_id =", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotEqualTo(Long value) {
            addCriterion("company_id <>", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThan(Long value) {
            addCriterion("company_id >", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThanOrEqualTo(Long value) {
            addCriterion("company_id >=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThan(Long value) {
            addCriterion("company_id <", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThanOrEqualTo(Long value) {
            addCriterion("company_id <=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIn(List<Long> values) {
            addCriterion("company_id in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotIn(List<Long> values) {
            addCriterion("company_id not in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdBetween(Long value1, Long value2) {
            addCriterion("company_id between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotBetween(Long value1, Long value2) {
            addCriterion("company_id not between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationIsNull() {
            addCriterion("company_abbreviation is null");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationIsNotNull() {
            addCriterion("company_abbreviation is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationEqualTo(String value) {
            addCriterion("company_abbreviation =", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotEqualTo(String value) {
            addCriterion("company_abbreviation <>", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationGreaterThan(String value) {
            addCriterion("company_abbreviation >", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationGreaterThanOrEqualTo(String value) {
            addCriterion("company_abbreviation >=", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationLessThan(String value) {
            addCriterion("company_abbreviation <", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationLessThanOrEqualTo(String value) {
            addCriterion("company_abbreviation <=", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationLike(String value) {
            addCriterion("company_abbreviation like", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotLike(String value) {
            addCriterion("company_abbreviation not like", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationIn(List<String> values) {
            addCriterion("company_abbreviation in", values, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotIn(List<String> values) {
            addCriterion("company_abbreviation not in", values, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationBetween(String value1, String value2) {
            addCriterion("company_abbreviation between", value1, value2, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotBetween(String value1, String value2) {
            addCriterion("company_abbreviation not between", value1, value2, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andGoodsSumIsNull() {
            addCriterion("goods_sum is null");
            return (Criteria) this;
        }

        public Criteria andGoodsSumIsNotNull() {
            addCriterion("goods_sum is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsSumEqualTo(Integer value) {
            addCriterion("goods_sum =", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumNotEqualTo(Integer value) {
            addCriterion("goods_sum <>", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumGreaterThan(Integer value) {
            addCriterion("goods_sum >", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_sum >=", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumLessThan(Integer value) {
            addCriterion("goods_sum <", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumLessThanOrEqualTo(Integer value) {
            addCriterion("goods_sum <=", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumIn(List<Integer> values) {
            addCriterion("goods_sum in", values, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumNotIn(List<Integer> values) {
            addCriterion("goods_sum not in", values, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumBetween(Integer value1, Integer value2) {
            addCriterion("goods_sum between", value1, value2, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_sum not between", value1, value2, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountIsNull() {
            addCriterion("actual_pay_amount is null");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountIsNotNull() {
            addCriterion("actual_pay_amount is not null");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountEqualTo(BigDecimal value) {
            addCriterion("actual_pay_amount =", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountNotEqualTo(BigDecimal value) {
            addCriterion("actual_pay_amount <>", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountGreaterThan(BigDecimal value) {
            addCriterion("actual_pay_amount >", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("actual_pay_amount >=", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountLessThan(BigDecimal value) {
            addCriterion("actual_pay_amount <", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("actual_pay_amount <=", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountIn(List<BigDecimal> values) {
            addCriterion("actual_pay_amount in", values, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountNotIn(List<BigDecimal> values) {
            addCriterion("actual_pay_amount not in", values, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("actual_pay_amount between", value1, value2, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("actual_pay_amount not between", value1, value2, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andIsPrintIsNull() {
            addCriterion("is_print is null");
            return (Criteria) this;
        }

        public Criteria andIsPrintIsNotNull() {
            addCriterion("is_print is not null");
            return (Criteria) this;
        }

        public Criteria andIsPrintEqualTo(Boolean value) {
            addCriterion("is_print =", value, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintNotEqualTo(Boolean value) {
            addCriterion("is_print <>", value, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintGreaterThan(Boolean value) {
            addCriterion("is_print >", value, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_print >=", value, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintLessThan(Boolean value) {
            addCriterion("is_print <", value, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintLessThanOrEqualTo(Boolean value) {
            addCriterion("is_print <=", value, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintIn(List<Boolean> values) {
            addCriterion("is_print in", values, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintNotIn(List<Boolean> values) {
            addCriterion("is_print not in", values, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintBetween(Boolean value1, Boolean value2) {
            addCriterion("is_print between", value1, value2, "isPrint");
            return (Criteria) this;
        }

        public Criteria andIsPrintNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_print not between", value1, value2, "isPrint");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Boolean value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Boolean value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Boolean value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Boolean value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Boolean> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Boolean> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNull() {
            addCriterion("create_by is null");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNotNull() {
            addCriterion("create_by is not null");
            return (Criteria) this;
        }

        public Criteria andCreateByEqualTo(String value) {
            addCriterion("create_by =", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotEqualTo(String value) {
            addCriterion("create_by <>", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThan(String value) {
            addCriterion("create_by >", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThanOrEqualTo(String value) {
            addCriterion("create_by >=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThan(String value) {
            addCriterion("create_by <", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThanOrEqualTo(String value) {
            addCriterion("create_by <=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLike(String value) {
            addCriterion("create_by like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotLike(String value) {
            addCriterion("create_by not like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByIn(List<String> values) {
            addCriterion("create_by in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotIn(List<String> values) {
            addCriterion("create_by not in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByBetween(String value1, String value2) {
            addCriterion("create_by between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotBetween(String value1, String value2) {
            addCriterion("create_by not between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNull() {
            addCriterion("update_by is null");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNotNull() {
            addCriterion("update_by is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateByEqualTo(String value) {
            addCriterion("update_by =", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotEqualTo(String value) {
            addCriterion("update_by <>", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThan(String value) {
            addCriterion("update_by >", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
            addCriterion("update_by >=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThan(String value) {
            addCriterion("update_by <", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThanOrEqualTo(String value) {
            addCriterion("update_by <=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLike(String value) {
            addCriterion("update_by like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotLike(String value) {
            addCriterion("update_by not like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByIn(List<String> values) {
            addCriterion("update_by in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotIn(List<String> values) {
            addCriterion("update_by not in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByBetween(String value1, String value2) {
            addCriterion("update_by between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotBetween(String value1, String value2) {
            addCriterion("update_by not between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}