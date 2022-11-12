/* https://github.com/orange1438 */
package com.milisong.dms.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ShunfengOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShunfengOrderExample() {
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

    protected abstract static class BaseCriteria {
        protected List<Criterion> criteria;

        protected BaseCriteria() {
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

        public Criteria andSfOrderIdIsNull() {
            addCriterion("sf_order_id is null");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdIsNotNull() {
            addCriterion("sf_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdEqualTo(Long value) {
            addCriterion("sf_order_id =", value, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdNotEqualTo(Long value) {
            addCriterion("sf_order_id <>", value, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdGreaterThan(Long value) {
            addCriterion("sf_order_id >", value, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("sf_order_id >=", value, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdLessThan(Long value) {
            addCriterion("sf_order_id <", value, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("sf_order_id <=", value, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdIn(List<Long> values) {
            addCriterion("sf_order_id in", values, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdNotIn(List<Long> values) {
            addCriterion("sf_order_id not in", values, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdBetween(Long value1, Long value2) {
            addCriterion("sf_order_id between", value1, value2, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andSfOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("sf_order_id not between", value1, value2, "sfOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdIsNull() {
            addCriterion("shop_order_id is null");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdIsNotNull() {
            addCriterion("shop_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdEqualTo(Long value) {
            addCriterion("shop_order_id =", value, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdNotEqualTo(Long value) {
            addCriterion("shop_order_id <>", value, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdGreaterThan(Long value) {
            addCriterion("shop_order_id >", value, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("shop_order_id >=", value, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdLessThan(Long value) {
            addCriterion("shop_order_id <", value, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("shop_order_id <=", value, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdIn(List<Long> values) {
            addCriterion("shop_order_id in", values, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdNotIn(List<Long> values) {
            addCriterion("shop_order_id not in", values, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdBetween(Long value1, Long value2) {
            addCriterion("shop_order_id between", value1, value2, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andShopOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("shop_order_id not between", value1, value2, "shopOrderId");
            return (Criteria) this;
        }

        public Criteria andExpectTimeIsNull() {
            addCriterion("expect_time is null");
            return (Criteria) this;
        }

        public Criteria andExpectTimeIsNotNull() {
            addCriterion("expect_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpectTimeEqualTo(Date value) {
            addCriterionForJDBCTime("expect_time =", value, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("expect_time <>", value, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("expect_time >", value, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("expect_time >=", value, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeLessThan(Date value) {
            addCriterionForJDBCTime("expect_time <", value, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("expect_time <=", value, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeIn(List<Date> values) {
            addCriterionForJDBCTime("expect_time in", values, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("expect_time not in", values, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("expect_time between", value1, value2, "expectTime");
            return (Criteria) this;
        }

        public Criteria andExpectTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("expect_time not between", value1, value2, "expectTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateIsNull() {
            addCriterion("delivery_date is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateIsNotNull() {
            addCriterion("delivery_date is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateEqualTo(Date value) {
            addCriterion("delivery_date =", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateNotEqualTo(Date value) {
            addCriterion("delivery_date <>", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateGreaterThan(Date value) {
            addCriterion("delivery_date >", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateGreaterThanOrEqualTo(Date value) {
            addCriterion("delivery_date >=", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateLessThan(Date value) {
            addCriterion("delivery_date <", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateLessThanOrEqualTo(Date value) {
            addCriterion("delivery_date <=", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateIn(List<Date> values) {
            addCriterion("delivery_date in", values, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateNotIn(List<Date> values) {
            addCriterion("delivery_date not in", values, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateBetween(Date value1, Date value2) {
            addCriterion("delivery_date between", value1, value2, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateNotBetween(Date value1, Date value2) {
            addCriterion("delivery_date not between", value1, value2, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeIsNull() {
            addCriterion("distribute_time is null");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeIsNotNull() {
            addCriterion("distribute_time is not null");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeEqualTo(Date value) {
            addCriterion("distribute_time =", value, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeNotEqualTo(Date value) {
            addCriterion("distribute_time <>", value, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeGreaterThan(Date value) {
            addCriterion("distribute_time >", value, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("distribute_time >=", value, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeLessThan(Date value) {
            addCriterion("distribute_time <", value, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeLessThanOrEqualTo(Date value) {
            addCriterion("distribute_time <=", value, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeIn(List<Date> values) {
            addCriterion("distribute_time in", values, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeNotIn(List<Date> values) {
            addCriterion("distribute_time not in", values, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeBetween(Date value1, Date value2) {
            addCriterion("distribute_time between", value1, value2, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDistributeTimeNotBetween(Date value1, Date value2) {
            addCriterion("distribute_time not between", value1, value2, "distributeTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressIsNull() {
            addCriterion("delivery_address is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressIsNotNull() {
            addCriterion("delivery_address is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressEqualTo(String value) {
            addCriterion("delivery_address =", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotEqualTo(String value) {
            addCriterion("delivery_address <>", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressGreaterThan(String value) {
            addCriterion("delivery_address >", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressGreaterThanOrEqualTo(String value) {
            addCriterion("delivery_address >=", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressLessThan(String value) {
            addCriterion("delivery_address <", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressLessThanOrEqualTo(String value) {
            addCriterion("delivery_address <=", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressLike(String value) {
            addCriterion("delivery_address like", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotLike(String value) {
            addCriterion("delivery_address not like", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressIn(List<String> values) {
            addCriterion("delivery_address in", values, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotIn(List<String> values) {
            addCriterion("delivery_address not in", values, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressBetween(String value1, String value2) {
            addCriterion("delivery_address between", value1, value2, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotBetween(String value1, String value2) {
            addCriterion("delivery_address not between", value1, value2, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeIsNull() {
            addCriterion("confirm_time is null");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeIsNotNull() {
            addCriterion("confirm_time is not null");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeEqualTo(Date value) {
            addCriterion("confirm_time =", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeNotEqualTo(Date value) {
            addCriterion("confirm_time <>", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeGreaterThan(Date value) {
            addCriterion("confirm_time >", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("confirm_time >=", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeLessThan(Date value) {
            addCriterion("confirm_time <", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeLessThanOrEqualTo(Date value) {
            addCriterion("confirm_time <=", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeIn(List<Date> values) {
            addCriterion("confirm_time in", values, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeNotIn(List<Date> values) {
            addCriterion("confirm_time not in", values, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeBetween(Date value1, Date value2) {
            addCriterion("confirm_time between", value1, value2, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeNotBetween(Date value1, Date value2) {
            addCriterion("confirm_time not between", value1, value2, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andTransporterNameIsNull() {
            addCriterion("transporter_name is null");
            return (Criteria) this;
        }

        public Criteria andTransporterNameIsNotNull() {
            addCriterion("transporter_name is not null");
            return (Criteria) this;
        }

        public Criteria andTransporterNameEqualTo(String value) {
            addCriterion("transporter_name =", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameNotEqualTo(String value) {
            addCriterion("transporter_name <>", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameGreaterThan(String value) {
            addCriterion("transporter_name >", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameGreaterThanOrEqualTo(String value) {
            addCriterion("transporter_name >=", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameLessThan(String value) {
            addCriterion("transporter_name <", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameLessThanOrEqualTo(String value) {
            addCriterion("transporter_name <=", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameLike(String value) {
            addCriterion("transporter_name like", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameNotLike(String value) {
            addCriterion("transporter_name not like", value, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameIn(List<String> values) {
            addCriterion("transporter_name in", values, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameNotIn(List<String> values) {
            addCriterion("transporter_name not in", values, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameBetween(String value1, String value2) {
            addCriterion("transporter_name between", value1, value2, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterNameNotBetween(String value1, String value2) {
            addCriterion("transporter_name not between", value1, value2, "transporterName");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneIsNull() {
            addCriterion("transporter_phone is null");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneIsNotNull() {
            addCriterion("transporter_phone is not null");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneEqualTo(String value) {
            addCriterion("transporter_phone =", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneNotEqualTo(String value) {
            addCriterion("transporter_phone <>", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneGreaterThan(String value) {
            addCriterion("transporter_phone >", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("transporter_phone >=", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneLessThan(String value) {
            addCriterion("transporter_phone <", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneLessThanOrEqualTo(String value) {
            addCriterion("transporter_phone <=", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneLike(String value) {
            addCriterion("transporter_phone like", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneNotLike(String value) {
            addCriterion("transporter_phone not like", value, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneIn(List<String> values) {
            addCriterion("transporter_phone in", values, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneNotIn(List<String> values) {
            addCriterion("transporter_phone not in", values, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneBetween(String value1, String value2) {
            addCriterion("transporter_phone between", value1, value2, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andTransporterPhoneNotBetween(String value1, String value2) {
            addCriterion("transporter_phone not between", value1, value2, "transporterPhone");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeIsNull() {
            addCriterion("arrived_shop_time is null");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeIsNotNull() {
            addCriterion("arrived_shop_time is not null");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeEqualTo(Date value) {
            addCriterion("arrived_shop_time =", value, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeNotEqualTo(Date value) {
            addCriterion("arrived_shop_time <>", value, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeGreaterThan(Date value) {
            addCriterion("arrived_shop_time >", value, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("arrived_shop_time >=", value, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeLessThan(Date value) {
            addCriterion("arrived_shop_time <", value, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeLessThanOrEqualTo(Date value) {
            addCriterion("arrived_shop_time <=", value, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeIn(List<Date> values) {
            addCriterion("arrived_shop_time in", values, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeNotIn(List<Date> values) {
            addCriterion("arrived_shop_time not in", values, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeBetween(Date value1, Date value2) {
            addCriterion("arrived_shop_time between", value1, value2, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andArrivedShopTimeNotBetween(Date value1, Date value2) {
            addCriterion("arrived_shop_time not between", value1, value2, "arrivedShopTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeIsNull() {
            addCriterion("achieve_good_time is null");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeIsNotNull() {
            addCriterion("achieve_good_time is not null");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeEqualTo(Date value) {
            addCriterion("achieve_good_time =", value, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeNotEqualTo(Date value) {
            addCriterion("achieve_good_time <>", value, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeGreaterThan(Date value) {
            addCriterion("achieve_good_time >", value, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("achieve_good_time >=", value, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeLessThan(Date value) {
            addCriterion("achieve_good_time <", value, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeLessThanOrEqualTo(Date value) {
            addCriterion("achieve_good_time <=", value, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeIn(List<Date> values) {
            addCriterion("achieve_good_time in", values, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeNotIn(List<Date> values) {
            addCriterion("achieve_good_time not in", values, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeBetween(Date value1, Date value2) {
            addCriterion("achieve_good_time between", value1, value2, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andAchieveGoodTimeNotBetween(Date value1, Date value2) {
            addCriterion("achieve_good_time not between", value1, value2, "achieveGoodTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeIsNull() {
            addCriterion("arrived_time is null");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeIsNotNull() {
            addCriterion("arrived_time is not null");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeEqualTo(Date value) {
            addCriterion("arrived_time =", value, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeNotEqualTo(Date value) {
            addCriterion("arrived_time <>", value, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeGreaterThan(Date value) {
            addCriterion("arrived_time >", value, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("arrived_time >=", value, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeLessThan(Date value) {
            addCriterion("arrived_time <", value, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeLessThanOrEqualTo(Date value) {
            addCriterion("arrived_time <=", value, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeIn(List<Date> values) {
            addCriterion("arrived_time in", values, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeNotIn(List<Date> values) {
            addCriterion("arrived_time not in", values, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeBetween(Date value1, Date value2) {
            addCriterion("arrived_time between", value1, value2, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andArrivedTimeNotBetween(Date value1, Date value2) {
            addCriterion("arrived_time not between", value1, value2, "arrivedTime");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeIsNull() {
            addCriterion("business_type is null");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeIsNotNull() {
            addCriterion("business_type is not null");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeEqualTo(Byte value) {
            addCriterion("business_type =", value, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeNotEqualTo(Byte value) {
            addCriterion("business_type <>", value, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeGreaterThan(Byte value) {
            addCriterion("business_type >", value, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("business_type >=", value, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeLessThan(Byte value) {
            addCriterion("business_type <", value, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeLessThanOrEqualTo(Byte value) {
            addCriterion("business_type <=", value, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeIn(List<Byte> values) {
            addCriterion("business_type in", values, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeNotIn(List<Byte> values) {
            addCriterion("business_type not in", values, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeBetween(Byte value1, Byte value2) {
            addCriterion("business_type between", value1, value2, "businessType");
            return (Criteria) this;
        }

        public Criteria andBusinessTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("business_type not between", value1, value2, "businessType");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusIsNull() {
            addCriterion("notify_status is null");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusIsNotNull() {
            addCriterion("notify_status is not null");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusEqualTo(Byte value) {
            addCriterion("notify_status =", value, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusNotEqualTo(Byte value) {
            addCriterion("notify_status <>", value, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusGreaterThan(Byte value) {
            addCriterion("notify_status >", value, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("notify_status >=", value, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusLessThan(Byte value) {
            addCriterion("notify_status <", value, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusLessThanOrEqualTo(Byte value) {
            addCriterion("notify_status <=", value, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusIn(List<Byte> values) {
            addCriterion("notify_status in", values, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusNotIn(List<Byte> values) {
            addCriterion("notify_status not in", values, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusBetween(Byte value1, Byte value2) {
            addCriterion("notify_status between", value1, value2, "notifyStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("notify_status not between", value1, value2, "notifyStatus");
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

    /**
     * 顺丰订单信息表shunfeng_order的映射实体
     */
    public static class Criteria extends BaseCriteria {

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