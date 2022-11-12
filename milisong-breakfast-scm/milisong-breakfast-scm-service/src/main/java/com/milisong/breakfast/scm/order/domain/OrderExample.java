package com.milisong.breakfast.scm.order.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderExample {
    /**
     * order
     */
    protected String orderByClause;

    /**
     * order
     */
    protected boolean distinct;

    /**
     * order
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public OrderExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * order 2018-12-12
     */
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

        public Criteria andIdIsNull() {
            addCriterion("`id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("`id` =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("`id` <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("`id` >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`id` >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("`id` <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("`id` <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("`id` in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("`id` not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("`id` between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("`id` not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNull() {
            addCriterion("`order_no` is null");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNotNull() {
            addCriterion("`order_no` is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNoEqualTo(String value) {
            addCriterion("`order_no` =", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotEqualTo(String value) {
            addCriterion("`order_no` <>", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThan(String value) {
            addCriterion("`order_no` >", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("`order_no` >=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThan(String value) {
            addCriterion("`order_no` <", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThanOrEqualTo(String value) {
            addCriterion("`order_no` <=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLike(String value) {
            addCriterion("`order_no` like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotLike(String value) {
            addCriterion("`order_no` not like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoIn(List<String> values) {
            addCriterion("`order_no` in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotIn(List<String> values) {
            addCriterion("`order_no` not in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoBetween(String value1, String value2) {
            addCriterion("`order_no` between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotBetween(String value1, String value2) {
            addCriterion("`order_no` not between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNull() {
            addCriterion("`shop_id` is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("`shop_id` is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(Long value) {
            addCriterion("`shop_id` =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Long value) {
            addCriterion("`shop_id` <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Long value) {
            addCriterion("`shop_id` >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`shop_id` >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Long value) {
            addCriterion("`shop_id` <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Long value) {
            addCriterion("`shop_id` <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Long> values) {
            addCriterion("`shop_id` in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Long> values) {
            addCriterion("`shop_id` not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Long value1, Long value2) {
            addCriterion("`shop_id` between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Long value1, Long value2) {
            addCriterion("`shop_id` not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNull() {
            addCriterion("`shop_code` is null");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNotNull() {
            addCriterion("`shop_code` is not null");
            return (Criteria) this;
        }

        public Criteria andShopCodeEqualTo(String value) {
            addCriterion("`shop_code` =", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotEqualTo(String value) {
            addCriterion("`shop_code` <>", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThan(String value) {
            addCriterion("`shop_code` >", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThanOrEqualTo(String value) {
            addCriterion("`shop_code` >=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThan(String value) {
            addCriterion("`shop_code` <", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThanOrEqualTo(String value) {
            addCriterion("`shop_code` <=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLike(String value) {
            addCriterion("`shop_code` like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotLike(String value) {
            addCriterion("`shop_code` not like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeIn(List<String> values) {
            addCriterion("`shop_code` in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotIn(List<String> values) {
            addCriterion("`shop_code` not in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeBetween(String value1, String value2) {
            addCriterion("`shop_code` between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotBetween(String value1, String value2) {
            addCriterion("`shop_code` not between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("`user_id` is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("`user_id` is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("`user_id` =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("`user_id` <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("`user_id` >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`user_id` >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("`user_id` <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("`user_id` <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("`user_id` in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("`user_id` not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("`user_id` between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("`user_id` not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNull() {
            addCriterion("`real_name` is null");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNotNull() {
            addCriterion("`real_name` is not null");
            return (Criteria) this;
        }

        public Criteria andRealNameEqualTo(String value) {
            addCriterion("`real_name` =", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotEqualTo(String value) {
            addCriterion("`real_name` <>", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThan(String value) {
            addCriterion("`real_name` >", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThanOrEqualTo(String value) {
            addCriterion("`real_name` >=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThan(String value) {
            addCriterion("`real_name` <", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThanOrEqualTo(String value) {
            addCriterion("`real_name` <=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLike(String value) {
            addCriterion("`real_name` like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotLike(String value) {
            addCriterion("`real_name` not like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameIn(List<String> values) {
            addCriterion("`real_name` in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotIn(List<String> values) {
            addCriterion("`real_name` not in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameBetween(String value1, String value2) {
            addCriterion("`real_name` between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotBetween(String value1, String value2) {
            addCriterion("`real_name` not between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andMobileNoIsNull() {
            addCriterion("`mobile_no` is null");
            return (Criteria) this;
        }

        public Criteria andMobileNoIsNotNull() {
            addCriterion("`mobile_no` is not null");
            return (Criteria) this;
        }

        public Criteria andMobileNoEqualTo(String value) {
            addCriterion("`mobile_no` =", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotEqualTo(String value) {
            addCriterion("`mobile_no` <>", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoGreaterThan(String value) {
            addCriterion("`mobile_no` >", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoGreaterThanOrEqualTo(String value) {
            addCriterion("`mobile_no` >=", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLessThan(String value) {
            addCriterion("`mobile_no` <", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLessThanOrEqualTo(String value) {
            addCriterion("`mobile_no` <=", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLike(String value) {
            addCriterion("`mobile_no` like", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotLike(String value) {
            addCriterion("`mobile_no` not like", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoIn(List<String> values) {
            addCriterion("`mobile_no` in", values, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotIn(List<String> values) {
            addCriterion("`mobile_no` not in", values, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoBetween(String value1, String value2) {
            addCriterion("`mobile_no` between", value1, value2, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotBetween(String value1, String value2) {
            addCriterion("`mobile_no` not between", value1, value2, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateIsNull() {
            addCriterion("`delivery_start_date` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateIsNotNull() {
            addCriterion("`delivery_start_date` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateEqualTo(Date value) {
            addCriterion("`delivery_start_date` =", value, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateNotEqualTo(Date value) {
            addCriterion("`delivery_start_date` <>", value, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateGreaterThan(Date value) {
            addCriterion("`delivery_start_date` >", value, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateGreaterThanOrEqualTo(Date value) {
            addCriterion("`delivery_start_date` >=", value, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateLessThan(Date value) {
            addCriterion("`delivery_start_date` <", value, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateLessThanOrEqualTo(Date value) {
            addCriterion("`delivery_start_date` <=", value, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateIn(List<Date> values) {
            addCriterion("`delivery_start_date` in", values, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateNotIn(List<Date> values) {
            addCriterion("`delivery_start_date` not in", values, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateBetween(Date value1, Date value2) {
            addCriterion("`delivery_start_date` between", value1, value2, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryStartDateNotBetween(Date value1, Date value2) {
            addCriterion("`delivery_start_date` not between", value1, value2, "deliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateIsNull() {
            addCriterion("`delivery_end_date` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateIsNotNull() {
            addCriterion("`delivery_end_date` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateEqualTo(Date value) {
            addCriterion("`delivery_end_date` =", value, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateNotEqualTo(Date value) {
            addCriterion("`delivery_end_date` <>", value, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateGreaterThan(Date value) {
            addCriterion("`delivery_end_date` >", value, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateGreaterThanOrEqualTo(Date value) {
            addCriterion("`delivery_end_date` >=", value, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateLessThan(Date value) {
            addCriterion("`delivery_end_date` <", value, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateLessThanOrEqualTo(Date value) {
            addCriterion("`delivery_end_date` <=", value, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateIn(List<Date> values) {
            addCriterion("`delivery_end_date` in", values, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateNotIn(List<Date> values) {
            addCriterion("`delivery_end_date` not in", values, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateBetween(Date value1, Date value2) {
            addCriterion("`delivery_end_date` between", value1, value2, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryEndDateNotBetween(Date value1, Date value2) {
            addCriterion("`delivery_end_date` not between", value1, value2, "deliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdIsNull() {
            addCriterion("`delivery_office_building_id` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdIsNotNull() {
            addCriterion("`delivery_office_building_id` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdEqualTo(Long value) {
            addCriterion("`delivery_office_building_id` =", value, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdNotEqualTo(Long value) {
            addCriterion("`delivery_office_building_id` <>", value, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdGreaterThan(Long value) {
            addCriterion("`delivery_office_building_id` >", value, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`delivery_office_building_id` >=", value, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdLessThan(Long value) {
            addCriterion("`delivery_office_building_id` <", value, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdLessThanOrEqualTo(Long value) {
            addCriterion("`delivery_office_building_id` <=", value, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdIn(List<Long> values) {
            addCriterion("`delivery_office_building_id` in", values, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdNotIn(List<Long> values) {
            addCriterion("`delivery_office_building_id` not in", values, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdBetween(Long value1, Long value2) {
            addCriterion("`delivery_office_building_id` between", value1, value2, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryOfficeBuildingIdNotBetween(Long value1, Long value2) {
            addCriterion("`delivery_office_building_id` not between", value1, value2, "deliveryOfficeBuildingId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdIsNull() {
            addCriterion("`delivery_company_id` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdIsNotNull() {
            addCriterion("`delivery_company_id` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdEqualTo(Long value) {
            addCriterion("`delivery_company_id` =", value, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdNotEqualTo(Long value) {
            addCriterion("`delivery_company_id` <>", value, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdGreaterThan(Long value) {
            addCriterion("`delivery_company_id` >", value, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`delivery_company_id` >=", value, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdLessThan(Long value) {
            addCriterion("`delivery_company_id` <", value, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdLessThanOrEqualTo(Long value) {
            addCriterion("`delivery_company_id` <=", value, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdIn(List<Long> values) {
            addCriterion("`delivery_company_id` in", values, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdNotIn(List<Long> values) {
            addCriterion("`delivery_company_id` not in", values, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdBetween(Long value1, Long value2) {
            addCriterion("`delivery_company_id` between", value1, value2, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIdNotBetween(Long value1, Long value2) {
            addCriterion("`delivery_company_id` not between", value1, value2, "deliveryCompanyId");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIsNull() {
            addCriterion("`delivery_company` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIsNotNull() {
            addCriterion("`delivery_company` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyEqualTo(String value) {
            addCriterion("`delivery_company` =", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyNotEqualTo(String value) {
            addCriterion("`delivery_company` <>", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyGreaterThan(String value) {
            addCriterion("`delivery_company` >", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyGreaterThanOrEqualTo(String value) {
            addCriterion("`delivery_company` >=", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyLessThan(String value) {
            addCriterion("`delivery_company` <", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyLessThanOrEqualTo(String value) {
            addCriterion("`delivery_company` <=", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyLike(String value) {
            addCriterion("`delivery_company` like", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyNotLike(String value) {
            addCriterion("`delivery_company` not like", value, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyIn(List<String> values) {
            addCriterion("`delivery_company` in", values, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyNotIn(List<String> values) {
            addCriterion("`delivery_company` not in", values, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyBetween(String value1, String value2) {
            addCriterion("`delivery_company` between", value1, value2, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryCompanyNotBetween(String value1, String value2) {
            addCriterion("`delivery_company` not between", value1, value2, "deliveryCompany");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressIsNull() {
            addCriterion("`delivery_address` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressIsNotNull() {
            addCriterion("`delivery_address` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressEqualTo(String value) {
            addCriterion("`delivery_address` =", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotEqualTo(String value) {
            addCriterion("`delivery_address` <>", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressGreaterThan(String value) {
            addCriterion("`delivery_address` >", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressGreaterThanOrEqualTo(String value) {
            addCriterion("`delivery_address` >=", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressLessThan(String value) {
            addCriterion("`delivery_address` <", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressLessThanOrEqualTo(String value) {
            addCriterion("`delivery_address` <=", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressLike(String value) {
            addCriterion("`delivery_address` like", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotLike(String value) {
            addCriterion("`delivery_address` not like", value, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressIn(List<String> values) {
            addCriterion("`delivery_address` in", values, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotIn(List<String> values) {
            addCriterion("`delivery_address` not in", values, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressBetween(String value1, String value2) {
            addCriterion("`delivery_address` between", value1, value2, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryAddressNotBetween(String value1, String value2) {
            addCriterion("`delivery_address` not between", value1, value2, "deliveryAddress");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIsNull() {
            addCriterion("`delivery_floor` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIsNotNull() {
            addCriterion("`delivery_floor` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorEqualTo(String value) {
            addCriterion("`delivery_floor` =", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotEqualTo(String value) {
            addCriterion("`delivery_floor` <>", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorGreaterThan(String value) {
            addCriterion("`delivery_floor` >", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorGreaterThanOrEqualTo(String value) {
            addCriterion("`delivery_floor` >=", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLessThan(String value) {
            addCriterion("`delivery_floor` <", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLessThanOrEqualTo(String value) {
            addCriterion("`delivery_floor` <=", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLike(String value) {
            addCriterion("`delivery_floor` like", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotLike(String value) {
            addCriterion("`delivery_floor` not like", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIn(List<String> values) {
            addCriterion("`delivery_floor` in", values, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotIn(List<String> values) {
            addCriterion("`delivery_floor` not in", values, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorBetween(String value1, String value2) {
            addCriterion("`delivery_floor` between", value1, value2, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotBetween(String value1, String value2) {
            addCriterion("`delivery_floor` not between", value1, value2, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomIsNull() {
            addCriterion("`delivery_room` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomIsNotNull() {
            addCriterion("`delivery_room` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomEqualTo(String value) {
            addCriterion("`delivery_room` =", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomNotEqualTo(String value) {
            addCriterion("`delivery_room` <>", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomGreaterThan(String value) {
            addCriterion("`delivery_room` >", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomGreaterThanOrEqualTo(String value) {
            addCriterion("`delivery_room` >=", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomLessThan(String value) {
            addCriterion("`delivery_room` <", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomLessThanOrEqualTo(String value) {
            addCriterion("`delivery_room` <=", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomLike(String value) {
            addCriterion("`delivery_room` like", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomNotLike(String value) {
            addCriterion("`delivery_room` not like", value, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomIn(List<String> values) {
            addCriterion("`delivery_room` in", values, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomNotIn(List<String> values) {
            addCriterion("`delivery_room` not in", values, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomBetween(String value1, String value2) {
            addCriterion("`delivery_room` between", value1, value2, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andDeliveryRoomNotBetween(String value1, String value2) {
            addCriterion("`delivery_room` not between", value1, value2, "deliveryRoom");
            return (Criteria) this;
        }

        public Criteria andGoodsSumIsNull() {
            addCriterion("`goods_sum` is null");
            return (Criteria) this;
        }

        public Criteria andGoodsSumIsNotNull() {
            addCriterion("`goods_sum` is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsSumEqualTo(Integer value) {
            addCriterion("`goods_sum` =", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumNotEqualTo(Integer value) {
            addCriterion("`goods_sum` <>", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumGreaterThan(Integer value) {
            addCriterion("`goods_sum` >", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumGreaterThanOrEqualTo(Integer value) {
            addCriterion("`goods_sum` >=", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumLessThan(Integer value) {
            addCriterion("`goods_sum` <", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumLessThanOrEqualTo(Integer value) {
            addCriterion("`goods_sum` <=", value, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumIn(List<Integer> values) {
            addCriterion("`goods_sum` in", values, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumNotIn(List<Integer> values) {
            addCriterion("`goods_sum` not in", values, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumBetween(Integer value1, Integer value2) {
            addCriterion("`goods_sum` between", value1, value2, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andGoodsSumNotBetween(Integer value1, Integer value2) {
            addCriterion("`goods_sum` not between", value1, value2, "goodsSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumIsNull() {
            addCriterion("`sku_sum` is null");
            return (Criteria) this;
        }

        public Criteria andSkuSumIsNotNull() {
            addCriterion("`sku_sum` is not null");
            return (Criteria) this;
        }

        public Criteria andSkuSumEqualTo(Integer value) {
            addCriterion("`sku_sum` =", value, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumNotEqualTo(Integer value) {
            addCriterion("`sku_sum` <>", value, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumGreaterThan(Integer value) {
            addCriterion("`sku_sum` >", value, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumGreaterThanOrEqualTo(Integer value) {
            addCriterion("`sku_sum` >=", value, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumLessThan(Integer value) {
            addCriterion("`sku_sum` <", value, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumLessThanOrEqualTo(Integer value) {
            addCriterion("`sku_sum` <=", value, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumIn(List<Integer> values) {
            addCriterion("`sku_sum` in", values, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumNotIn(List<Integer> values) {
            addCriterion("`sku_sum` not in", values, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumBetween(Integer value1, Integer value2) {
            addCriterion("`sku_sum` between", value1, value2, "skuSum");
            return (Criteria) this;
        }

        public Criteria andSkuSumNotBetween(Integer value1, Integer value2) {
            addCriterion("`sku_sum` not between", value1, value2, "skuSum");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNull() {
            addCriterion("`total_amount` is null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNotNull() {
            addCriterion("`total_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountEqualTo(BigDecimal value) {
            addCriterion("`total_amount` =", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotEqualTo(BigDecimal value) {
            addCriterion("`total_amount` <>", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThan(BigDecimal value) {
            addCriterion("`total_amount` >", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`total_amount` >=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThan(BigDecimal value) {
            addCriterion("`total_amount` <", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`total_amount` <=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIn(List<BigDecimal> values) {
            addCriterion("`total_amount` in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotIn(List<BigDecimal> values) {
            addCriterion("`total_amount` not in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`total_amount` between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`total_amount` not between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountIsNull() {
            addCriterion("`actual_pay_amount` is null");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountIsNotNull() {
            addCriterion("`actual_pay_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountEqualTo(BigDecimal value) {
            addCriterion("`actual_pay_amount` =", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountNotEqualTo(BigDecimal value) {
            addCriterion("`actual_pay_amount` <>", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountGreaterThan(BigDecimal value) {
            addCriterion("`actual_pay_amount` >", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`actual_pay_amount` >=", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountLessThan(BigDecimal value) {
            addCriterion("`actual_pay_amount` <", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`actual_pay_amount` <=", value, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountIn(List<BigDecimal> values) {
            addCriterion("`actual_pay_amount` in", values, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountNotIn(List<BigDecimal> values) {
            addCriterion("`actual_pay_amount` not in", values, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`actual_pay_amount` between", value1, value2, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andActualPayAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`actual_pay_amount` not between", value1, value2, "actualPayAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountIsNull() {
            addCriterion("`delivery_cost_amount` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountIsNotNull() {
            addCriterion("`delivery_cost_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountEqualTo(BigDecimal value) {
            addCriterion("`delivery_cost_amount` =", value, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountNotEqualTo(BigDecimal value) {
            addCriterion("`delivery_cost_amount` <>", value, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountGreaterThan(BigDecimal value) {
            addCriterion("`delivery_cost_amount` >", value, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`delivery_cost_amount` >=", value, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountLessThan(BigDecimal value) {
            addCriterion("`delivery_cost_amount` <", value, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`delivery_cost_amount` <=", value, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountIn(List<BigDecimal> values) {
            addCriterion("`delivery_cost_amount` in", values, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountNotIn(List<BigDecimal> values) {
            addCriterion("`delivery_cost_amount` not in", values, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`delivery_cost_amount` between", value1, value2, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDeliveryCostAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`delivery_cost_amount` not between", value1, value2, "deliveryCostAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIsNull() {
            addCriterion("`discount_amount` is null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIsNotNull() {
            addCriterion("`discount_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountEqualTo(BigDecimal value) {
            addCriterion("`discount_amount` =", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotEqualTo(BigDecimal value) {
            addCriterion("`discount_amount` <>", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThan(BigDecimal value) {
            addCriterion("`discount_amount` >", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`discount_amount` >=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThan(BigDecimal value) {
            addCriterion("`discount_amount` <", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`discount_amount` <=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIn(List<BigDecimal> values) {
            addCriterion("`discount_amount` in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotIn(List<BigDecimal> values) {
            addCriterion("`discount_amount` not in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`discount_amount` between", value1, value2, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`discount_amount` not between", value1, value2, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountIsNull() {
            addCriterion("`package_amount` is null");
            return (Criteria) this;
        }

        public Criteria andPackageAmountIsNotNull() {
            addCriterion("`package_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andPackageAmountEqualTo(BigDecimal value) {
            addCriterion("`package_amount` =", value, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountNotEqualTo(BigDecimal value) {
            addCriterion("`package_amount` <>", value, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountGreaterThan(BigDecimal value) {
            addCriterion("`package_amount` >", value, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`package_amount` >=", value, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountLessThan(BigDecimal value) {
            addCriterion("`package_amount` <", value, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`package_amount` <=", value, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountIn(List<BigDecimal> values) {
            addCriterion("`package_amount` in", values, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountNotIn(List<BigDecimal> values) {
            addCriterion("`package_amount` not in", values, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`package_amount` between", value1, value2, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andPackageAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`package_amount` not between", value1, value2, "packageAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountIsNull() {
            addCriterion("`red_packet_amount` is null");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountIsNotNull() {
            addCriterion("`red_packet_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountEqualTo(BigDecimal value) {
            addCriterion("`red_packet_amount` =", value, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountNotEqualTo(BigDecimal value) {
            addCriterion("`red_packet_amount` <>", value, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountGreaterThan(BigDecimal value) {
            addCriterion("`red_packet_amount` >", value, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`red_packet_amount` >=", value, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountLessThan(BigDecimal value) {
            addCriterion("`red_packet_amount` <", value, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`red_packet_amount` <=", value, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountIn(List<BigDecimal> values) {
            addCriterion("`red_packet_amount` in", values, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountNotIn(List<BigDecimal> values) {
            addCriterion("`red_packet_amount` not in", values, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`red_packet_amount` between", value1, value2, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andRedPacketAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`red_packet_amount` not between", value1, value2, "redPacketAmount");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIsNull() {
            addCriterion("`complete_time` is null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIsNotNull() {
            addCriterion("`complete_time` is not null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeEqualTo(Date value) {
            addCriterion("`complete_time` =", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotEqualTo(Date value) {
            addCriterion("`complete_time` <>", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThan(Date value) {
            addCriterion("`complete_time` >", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`complete_time` >=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThan(Date value) {
            addCriterion("`complete_time` <", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThanOrEqualTo(Date value) {
            addCriterion("`complete_time` <=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIn(List<Date> values) {
            addCriterion("`complete_time` in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotIn(List<Date> values) {
            addCriterion("`complete_time` not in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeBetween(Date value1, Date value2) {
            addCriterion("`complete_time` between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotBetween(Date value1, Date value2) {
            addCriterion("`complete_time` not between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNull() {
            addCriterion("`order_status` is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("`order_status` is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(Byte value) {
            addCriterion("`order_status` =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(Byte value) {
            addCriterion("`order_status` <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(Byte value) {
            addCriterion("`order_status` >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("`order_status` >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(Byte value) {
            addCriterion("`order_status` <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(Byte value) {
            addCriterion("`order_status` <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<Byte> values) {
            addCriterion("`order_status` in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<Byte> values) {
            addCriterion("`order_status` not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(Byte value1, Byte value2) {
            addCriterion("`order_status` between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("`order_status` not between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusIsNull() {
            addCriterion("`delivery_status` is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusIsNotNull() {
            addCriterion("`delivery_status` is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusEqualTo(Byte value) {
            addCriterion("`delivery_status` =", value, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusNotEqualTo(Byte value) {
            addCriterion("`delivery_status` <>", value, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusGreaterThan(Byte value) {
            addCriterion("`delivery_status` >", value, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("`delivery_status` >=", value, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusLessThan(Byte value) {
            addCriterion("`delivery_status` <", value, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusLessThanOrEqualTo(Byte value) {
            addCriterion("`delivery_status` <=", value, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusIn(List<Byte> values) {
            addCriterion("`delivery_status` in", values, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusNotIn(List<Byte> values) {
            addCriterion("`delivery_status` not in", values, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusBetween(Byte value1, Byte value2) {
            addCriterion("`delivery_status` between", value1, value2, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andDeliveryStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("`delivery_status` not between", value1, value2, "deliveryStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusIsNull() {
            addCriterion("`orderset_process_status` is null");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusIsNotNull() {
            addCriterion("`orderset_process_status` is not null");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusEqualTo(Boolean value) {
            addCriterion("`orderset_process_status` =", value, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusNotEqualTo(Boolean value) {
            addCriterion("`orderset_process_status` <>", value, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusGreaterThan(Boolean value) {
            addCriterion("`orderset_process_status` >", value, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusGreaterThanOrEqualTo(Boolean value) {
            addCriterion("`orderset_process_status` >=", value, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusLessThan(Boolean value) {
            addCriterion("`orderset_process_status` <", value, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusLessThanOrEqualTo(Boolean value) {
            addCriterion("`orderset_process_status` <=", value, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusIn(List<Boolean> values) {
            addCriterion("`orderset_process_status` in", values, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusNotIn(List<Boolean> values) {
            addCriterion("`orderset_process_status` not in", values, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusBetween(Boolean value1, Boolean value2) {
            addCriterion("`orderset_process_status` between", value1, value2, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andOrdersetProcessStatusNotBetween(Boolean value1, Boolean value2) {
            addCriterion("`orderset_process_status` not between", value1, value2, "ordersetProcessStatus");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("`create_time` is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("`create_time` is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("`create_time` =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("`create_time` <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("`create_time` >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`create_time` >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("`create_time` <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("`create_time` <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("`create_time` in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("`create_time` not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("`create_time` between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("`create_time` not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("`update_time` is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("`update_time` is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("`update_time` =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("`update_time` <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("`update_time` >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`update_time` >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("`update_time` <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("`update_time` <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("`update_time` in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("`update_time` not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("`update_time` between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("`update_time` not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNull() {
            addCriterion("`order_type` is null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNotNull() {
            addCriterion("`order_type` is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeEqualTo(Byte value) {
            addCriterion("`order_type` =", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotEqualTo(Byte value) {
            addCriterion("`order_type` <>", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThan(Byte value) {
            addCriterion("`order_type` >", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("`order_type` >=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThan(Byte value) {
            addCriterion("`order_type` <", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThanOrEqualTo(Byte value) {
            addCriterion("`order_type` <=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIn(List<Byte> values) {
            addCriterion("`order_type` in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotIn(List<Byte> values) {
            addCriterion("`order_type` not in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeBetween(Byte value1, Byte value2) {
            addCriterion("`order_type` between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("`order_type` not between", value1, value2, "orderType");
            return (Criteria) this;
        }
    }

    public static class Criteria extends BaseCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * order 2018-12-12
     */
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