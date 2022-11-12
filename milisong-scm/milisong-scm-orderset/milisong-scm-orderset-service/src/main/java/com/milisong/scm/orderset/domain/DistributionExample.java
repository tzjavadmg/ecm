package com.milisong.scm.orderset.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.milisong.scm.orderset.param.PageInfo;

public class DistributionExample extends PageInfo{
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DistributionExample() {
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

        public Criteria andDistributionNoIsNull() {
            addCriterion("distribution_no is null");
            return (Criteria) this;
        }

        public Criteria andDistributionNoIsNotNull() {
            addCriterion("distribution_no is not null");
            return (Criteria) this;
        }

        public Criteria andDistributionNoEqualTo(String value) {
            addCriterion("distribution_no =", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoNotEqualTo(String value) {
            addCriterion("distribution_no <>", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoGreaterThan(String value) {
            addCriterion("distribution_no >", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoGreaterThanOrEqualTo(String value) {
            addCriterion("distribution_no >=", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoLessThan(String value) {
            addCriterion("distribution_no <", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoLessThanOrEqualTo(String value) {
            addCriterion("distribution_no <=", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoLike(String value) {
            addCriterion("distribution_no like", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoNotLike(String value) {
            addCriterion("distribution_no not like", value, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoIn(List<String> values) {
            addCriterion("distribution_no in", values, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoNotIn(List<String> values) {
            addCriterion("distribution_no not in", values, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoBetween(String value1, String value2) {
            addCriterion("distribution_no between", value1, value2, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionNoNotBetween(String value1, String value2) {
            addCriterion("distribution_no not between", value1, value2, "distributionNo");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionIsNull() {
            addCriterion("distribution_description is null");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionIsNotNull() {
            addCriterion("distribution_description is not null");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionEqualTo(String value) {
            addCriterion("distribution_description =", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionNotEqualTo(String value) {
            addCriterion("distribution_description <>", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionGreaterThan(String value) {
            addCriterion("distribution_description >", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("distribution_description >=", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionLessThan(String value) {
            addCriterion("distribution_description <", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionLessThanOrEqualTo(String value) {
            addCriterion("distribution_description <=", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionLike(String value) {
            addCriterion("distribution_description like", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionNotLike(String value) {
            addCriterion("distribution_description not like", value, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionIn(List<String> values) {
            addCriterion("distribution_description in", values, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionNotIn(List<String> values) {
            addCriterion("distribution_description not in", values, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionBetween(String value1, String value2) {
            addCriterion("distribution_description between", value1, value2, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andDistributionDescriptionNotBetween(String value1, String value2) {
            addCriterion("distribution_description not between", value1, value2, "distributionDescription");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumIsNull() {
            addCriterion("orderset_sum is null");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumIsNotNull() {
            addCriterion("orderset_sum is not null");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumEqualTo(Integer value) {
            addCriterion("orderset_sum =", value, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumNotEqualTo(Integer value) {
            addCriterion("orderset_sum <>", value, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumGreaterThan(Integer value) {
            addCriterion("orderset_sum >", value, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumGreaterThanOrEqualTo(Integer value) {
            addCriterion("orderset_sum >=", value, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumLessThan(Integer value) {
            addCriterion("orderset_sum <", value, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumLessThanOrEqualTo(Integer value) {
            addCriterion("orderset_sum <=", value, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumIn(List<Integer> values) {
            addCriterion("orderset_sum in", values, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumNotIn(List<Integer> values) {
            addCriterion("orderset_sum not in", values, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumBetween(Integer value1, Integer value2) {
            addCriterion("orderset_sum between", value1, value2, "ordersetSum");
            return (Criteria) this;
        }

        public Criteria andOrdersetSumNotBetween(Integer value1, Integer value2) {
            addCriterion("orderset_sum not between", value1, value2, "ordersetSum");
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

        public Criteria andDistributorAccountIsNull() {
            addCriterion("distributor_account is null");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountIsNotNull() {
            addCriterion("distributor_account is not null");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountEqualTo(String value) {
            addCriterion("distributor_account =", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountNotEqualTo(String value) {
            addCriterion("distributor_account <>", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountGreaterThan(String value) {
            addCriterion("distributor_account >", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountGreaterThanOrEqualTo(String value) {
            addCriterion("distributor_account >=", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountLessThan(String value) {
            addCriterion("distributor_account <", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountLessThanOrEqualTo(String value) {
            addCriterion("distributor_account <=", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountLike(String value) {
            addCriterion("distributor_account like", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountNotLike(String value) {
            addCriterion("distributor_account not like", value, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountIn(List<String> values) {
            addCriterion("distributor_account in", values, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountNotIn(List<String> values) {
            addCriterion("distributor_account not in", values, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountBetween(String value1, String value2) {
            addCriterion("distributor_account between", value1, value2, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorAccountNotBetween(String value1, String value2) {
            addCriterion("distributor_account not between", value1, value2, "distributorAccount");
            return (Criteria) this;
        }

        public Criteria andDistributorNameIsNull() {
            addCriterion("distributor_name is null");
            return (Criteria) this;
        }

        public Criteria andDistributorNameIsNotNull() {
            addCriterion("distributor_name is not null");
            return (Criteria) this;
        }

        public Criteria andDistributorNameEqualTo(String value) {
            addCriterion("distributor_name =", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameNotEqualTo(String value) {
            addCriterion("distributor_name <>", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameGreaterThan(String value) {
            addCriterion("distributor_name >", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameGreaterThanOrEqualTo(String value) {
            addCriterion("distributor_name >=", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameLessThan(String value) {
            addCriterion("distributor_name <", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameLessThanOrEqualTo(String value) {
            addCriterion("distributor_name <=", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameLike(String value) {
            addCriterion("distributor_name like", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameNotLike(String value) {
            addCriterion("distributor_name not like", value, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameIn(List<String> values) {
            addCriterion("distributor_name in", values, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameNotIn(List<String> values) {
            addCriterion("distributor_name not in", values, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameBetween(String value1, String value2) {
            addCriterion("distributor_name between", value1, value2, "distributorName");
            return (Criteria) this;
        }

        public Criteria andDistributorNameNotBetween(String value1, String value2) {
            addCriterion("distributor_name not between", value1, value2, "distributorName");
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
            addCriterionForJDBCDate("delivery_date =", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("delivery_date <>", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateGreaterThan(Date value) {
            addCriterionForJDBCDate("delivery_date >", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("delivery_date >=", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateLessThan(Date value) {
            addCriterionForJDBCDate("delivery_date <", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("delivery_date <=", value, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateIn(List<Date> values) {
            addCriterionForJDBCDate("delivery_date in", values, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("delivery_date not in", values, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("delivery_date between", value1, value2, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andDeliveryDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("delivery_date not between", value1, value2, "deliveryDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateIsNull() {
            addCriterion("theory_delivery_start_date is null");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateIsNotNull() {
            addCriterion("theory_delivery_start_date is not null");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_start_date =", value, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateNotEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_start_date <>", value, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateGreaterThan(Date value) {
            addCriterionForJDBCTime("theory_delivery_start_date >", value, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_start_date >=", value, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateLessThan(Date value) {
            addCriterionForJDBCTime("theory_delivery_start_date <", value, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_start_date <=", value, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateIn(List<Date> values) {
            addCriterionForJDBCTime("theory_delivery_start_date in", values, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateNotIn(List<Date> values) {
            addCriterionForJDBCTime("theory_delivery_start_date not in", values, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("theory_delivery_start_date between", value1, value2, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryStartDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("theory_delivery_start_date not between", value1, value2, "theoryDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateIsNull() {
            addCriterion("theory_delivery_end_date is null");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateIsNotNull() {
            addCriterion("theory_delivery_end_date is not null");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_end_date =", value, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateNotEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_end_date <>", value, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateGreaterThan(Date value) {
            addCriterionForJDBCTime("theory_delivery_end_date >", value, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_end_date >=", value, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateLessThan(Date value) {
            addCriterionForJDBCTime("theory_delivery_end_date <", value, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("theory_delivery_end_date <=", value, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateIn(List<Date> values) {
            addCriterionForJDBCTime("theory_delivery_end_date in", values, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateNotIn(List<Date> values) {
            addCriterionForJDBCTime("theory_delivery_end_date not in", values, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("theory_delivery_end_date between", value1, value2, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andTheoryDeliveryEndDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("theory_delivery_end_date not between", value1, value2, "theoryDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateIsNull() {
            addCriterion("actual_delivery_start_date is null");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateIsNotNull() {
            addCriterion("actual_delivery_start_date is not null");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_start_date =", value, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateNotEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_start_date <>", value, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateGreaterThan(Date value) {
            addCriterionForJDBCTime("actual_delivery_start_date >", value, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_start_date >=", value, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateLessThan(Date value) {
            addCriterionForJDBCTime("actual_delivery_start_date <", value, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_start_date <=", value, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateIn(List<Date> values) {
            addCriterionForJDBCTime("actual_delivery_start_date in", values, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateNotIn(List<Date> values) {
            addCriterionForJDBCTime("actual_delivery_start_date not in", values, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("actual_delivery_start_date between", value1, value2, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryStartDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("actual_delivery_start_date not between", value1, value2, "actualDeliveryStartDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateIsNull() {
            addCriterion("actual_delivery_end_date is null");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateIsNotNull() {
            addCriterion("actual_delivery_end_date is not null");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_end_date =", value, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateNotEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_end_date <>", value, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateGreaterThan(Date value) {
            addCriterionForJDBCTime("actual_delivery_end_date >", value, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_end_date >=", value, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateLessThan(Date value) {
            addCriterionForJDBCTime("actual_delivery_end_date <", value, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("actual_delivery_end_date <=", value, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateIn(List<Date> values) {
            addCriterionForJDBCTime("actual_delivery_end_date in", values, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateNotIn(List<Date> values) {
            addCriterionForJDBCTime("actual_delivery_end_date not in", values, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("actual_delivery_end_date between", value1, value2, "actualDeliveryEndDate");
            return (Criteria) this;
        }

        public Criteria andActualDeliveryEndDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("actual_delivery_end_date not between", value1, value2, "actualDeliveryEndDate");
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

        public Criteria andIsPrintPickListIsNull() {
            addCriterion("is_print_pick_list is null");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListIsNotNull() {
            addCriterion("is_print_pick_list is not null");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListEqualTo(Boolean value) {
            addCriterion("is_print_pick_list =", value, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListNotEqualTo(Boolean value) {
            addCriterion("is_print_pick_list <>", value, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListGreaterThan(Boolean value) {
            addCriterion("is_print_pick_list >", value, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_print_pick_list >=", value, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListLessThan(Boolean value) {
            addCriterion("is_print_pick_list <", value, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListLessThanOrEqualTo(Boolean value) {
            addCriterion("is_print_pick_list <=", value, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListIn(List<Boolean> values) {
            addCriterion("is_print_pick_list in", values, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListNotIn(List<Boolean> values) {
            addCriterion("is_print_pick_list not in", values, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListBetween(Boolean value1, Boolean value2) {
            addCriterion("is_print_pick_list between", value1, value2, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintPickListNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_print_pick_list not between", value1, value2, "isPrintPickList");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionIsNull() {
            addCriterion("is_print_distribution is null");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionIsNotNull() {
            addCriterion("is_print_distribution is not null");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionEqualTo(Boolean value) {
            addCriterion("is_print_distribution =", value, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionNotEqualTo(Boolean value) {
            addCriterion("is_print_distribution <>", value, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionGreaterThan(Boolean value) {
            addCriterion("is_print_distribution >", value, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_print_distribution >=", value, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionLessThan(Boolean value) {
            addCriterion("is_print_distribution <", value, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionLessThanOrEqualTo(Boolean value) {
            addCriterion("is_print_distribution <=", value, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionIn(List<Boolean> values) {
            addCriterion("is_print_distribution in", values, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionNotIn(List<Boolean> values) {
            addCriterion("is_print_distribution not in", values, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionBetween(Boolean value1, Boolean value2) {
            addCriterion("is_print_distribution between", value1, value2, "isPrintDistribution");
            return (Criteria) this;
        }

        public Criteria andIsPrintDistributionNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_print_distribution not between", value1, value2, "isPrintDistribution");
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