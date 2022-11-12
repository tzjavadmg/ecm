package com.milisong.scm.goods.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodsScheduleDetailLunchExample {
    /**
     * goods_schedule_detail_lunch
     */
    protected String orderByClause;

    /**
     * goods_schedule_detail_lunch
     */
    protected boolean distinct;

    /**
     * goods_schedule_detail_lunch
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public GoodsScheduleDetailLunchExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-18
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
     * @mbg.generated 2018-12-18
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-18
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * goods_schedule_detail_lunch 2018-12-18
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

        public Criteria andShopCodeIsNull() {
            addCriterion("shop_code is null");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNotNull() {
            addCriterion("shop_code is not null");
            return (Criteria) this;
        }

        public Criteria andShopCodeEqualTo(String value) {
            addCriterion("shop_code =", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotEqualTo(String value) {
            addCriterion("shop_code <>", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThan(String value) {
            addCriterion("shop_code >", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThanOrEqualTo(String value) {
            addCriterion("shop_code >=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThan(String value) {
            addCriterion("shop_code <", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThanOrEqualTo(String value) {
            addCriterion("shop_code <=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLike(String value) {
            addCriterion("shop_code like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotLike(String value) {
            addCriterion("shop_code not like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeIn(List<String> values) {
            addCriterion("shop_code in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotIn(List<String> values) {
            addCriterion("shop_code not in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeBetween(String value1, String value2) {
            addCriterion("shop_code between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotBetween(String value1, String value2) {
            addCriterion("shop_code not between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopNameIsNull() {
            addCriterion("shop_name is null");
            return (Criteria) this;
        }

        public Criteria andShopNameIsNotNull() {
            addCriterion("shop_name is not null");
            return (Criteria) this;
        }

        public Criteria andShopNameEqualTo(String value) {
            addCriterion("shop_name =", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotEqualTo(String value) {
            addCriterion("shop_name <>", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameGreaterThan(String value) {
            addCriterion("shop_name >", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameGreaterThanOrEqualTo(String value) {
            addCriterion("shop_name >=", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLessThan(String value) {
            addCriterion("shop_name <", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLessThanOrEqualTo(String value) {
            addCriterion("shop_name <=", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLike(String value) {
            addCriterion("shop_name like", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotLike(String value) {
            addCriterion("shop_name not like", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameIn(List<String> values) {
            addCriterion("shop_name in", values, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotIn(List<String> values) {
            addCriterion("shop_name not in", values, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameBetween(String value1, String value2) {
            addCriterion("shop_name between", value1, value2, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotBetween(String value1, String value2) {
            addCriterion("shop_name not between", value1, value2, "shopName");
            return (Criteria) this;
        }

        public Criteria andYearIsNull() {
            addCriterion("year is null");
            return (Criteria) this;
        }

        public Criteria andYearIsNotNull() {
            addCriterion("year is not null");
            return (Criteria) this;
        }

        public Criteria andYearEqualTo(Integer value) {
            addCriterion("year =", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotEqualTo(Integer value) {
            addCriterion("year <>", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThan(Integer value) {
            addCriterion("year >", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThanOrEqualTo(Integer value) {
            addCriterion("year >=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThan(Integer value) {
            addCriterion("year <", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThanOrEqualTo(Integer value) {
            addCriterion("year <=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearIn(List<Integer> values) {
            addCriterion("year in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotIn(List<Integer> values) {
            addCriterion("year not in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearBetween(Integer value1, Integer value2) {
            addCriterion("year between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotBetween(Integer value1, Integer value2) {
            addCriterion("year not between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearIsNull() {
            addCriterion("week_of_year is null");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearIsNotNull() {
            addCriterion("week_of_year is not null");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearEqualTo(Integer value) {
            addCriterion("week_of_year =", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearNotEqualTo(Integer value) {
            addCriterion("week_of_year <>", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearGreaterThan(Integer value) {
            addCriterion("week_of_year >", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearGreaterThanOrEqualTo(Integer value) {
            addCriterion("week_of_year >=", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearLessThan(Integer value) {
            addCriterion("week_of_year <", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearLessThanOrEqualTo(Integer value) {
            addCriterion("week_of_year <=", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearIn(List<Integer> values) {
            addCriterion("week_of_year in", values, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearNotIn(List<Integer> values) {
            addCriterion("week_of_year not in", values, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearBetween(Integer value1, Integer value2) {
            addCriterion("week_of_year between", value1, value2, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearNotBetween(Integer value1, Integer value2) {
            addCriterion("week_of_year not between", value1, value2, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIsNull() {
            addCriterion("goods_code is null");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIsNotNull() {
            addCriterion("goods_code is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeEqualTo(String value) {
            addCriterion("goods_code =", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotEqualTo(String value) {
            addCriterion("goods_code <>", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeGreaterThan(String value) {
            addCriterion("goods_code >", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeGreaterThanOrEqualTo(String value) {
            addCriterion("goods_code >=", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLessThan(String value) {
            addCriterion("goods_code <", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLessThanOrEqualTo(String value) {
            addCriterion("goods_code <=", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLike(String value) {
            addCriterion("goods_code like", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotLike(String value) {
            addCriterion("goods_code not like", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIn(List<String> values) {
            addCriterion("goods_code in", values, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotIn(List<String> values) {
            addCriterion("goods_code not in", values, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeBetween(String value1, String value2) {
            addCriterion("goods_code between", value1, value2, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotBetween(String value1, String value2) {
            addCriterion("goods_code not between", value1, value2, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNull() {
            addCriterion("goods_name is null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNotNull() {
            addCriterion("goods_name is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameEqualTo(String value) {
            addCriterion("goods_name =", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotEqualTo(String value) {
            addCriterion("goods_name <>", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThan(String value) {
            addCriterion("goods_name >", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThanOrEqualTo(String value) {
            addCriterion("goods_name >=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThan(String value) {
            addCriterion("goods_name <", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThanOrEqualTo(String value) {
            addCriterion("goods_name <=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLike(String value) {
            addCriterion("goods_name like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotLike(String value) {
            addCriterion("goods_name not like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIn(List<String> values) {
            addCriterion("goods_name in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotIn(List<String> values) {
            addCriterion("goods_name not in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameBetween(String value1, String value2) {
            addCriterion("goods_name between", value1, value2, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotBetween(String value1, String value2) {
            addCriterion("goods_name not between", value1, value2, "goodsName");
            return (Criteria) this;
        }

        public Criteria andFlag1IsNull() {
            addCriterion("flag_1 is null");
            return (Criteria) this;
        }

        public Criteria andFlag1IsNotNull() {
            addCriterion("flag_1 is not null");
            return (Criteria) this;
        }

        public Criteria andFlag1EqualTo(Boolean value) {
            addCriterion("flag_1 =", value, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1NotEqualTo(Boolean value) {
            addCriterion("flag_1 <>", value, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1GreaterThan(Boolean value) {
            addCriterion("flag_1 >", value, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1GreaterThanOrEqualTo(Boolean value) {
            addCriterion("flag_1 >=", value, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1LessThan(Boolean value) {
            addCriterion("flag_1 <", value, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1LessThanOrEqualTo(Boolean value) {
            addCriterion("flag_1 <=", value, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1In(List<Boolean> values) {
            addCriterion("flag_1 in", values, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1NotIn(List<Boolean> values) {
            addCriterion("flag_1 not in", values, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1Between(Boolean value1, Boolean value2) {
            addCriterion("flag_1 between", value1, value2, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag1NotBetween(Boolean value1, Boolean value2) {
            addCriterion("flag_1 not between", value1, value2, "flag1");
            return (Criteria) this;
        }

        public Criteria andFlag2IsNull() {
            addCriterion("flag_2 is null");
            return (Criteria) this;
        }

        public Criteria andFlag2IsNotNull() {
            addCriterion("flag_2 is not null");
            return (Criteria) this;
        }

        public Criteria andFlag2EqualTo(Boolean value) {
            addCriterion("flag_2 =", value, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2NotEqualTo(Boolean value) {
            addCriterion("flag_2 <>", value, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2GreaterThan(Boolean value) {
            addCriterion("flag_2 >", value, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2GreaterThanOrEqualTo(Boolean value) {
            addCriterion("flag_2 >=", value, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2LessThan(Boolean value) {
            addCriterion("flag_2 <", value, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2LessThanOrEqualTo(Boolean value) {
            addCriterion("flag_2 <=", value, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2In(List<Boolean> values) {
            addCriterion("flag_2 in", values, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2NotIn(List<Boolean> values) {
            addCriterion("flag_2 not in", values, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2Between(Boolean value1, Boolean value2) {
            addCriterion("flag_2 between", value1, value2, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag2NotBetween(Boolean value1, Boolean value2) {
            addCriterion("flag_2 not between", value1, value2, "flag2");
            return (Criteria) this;
        }

        public Criteria andFlag3IsNull() {
            addCriterion("flag_3 is null");
            return (Criteria) this;
        }

        public Criteria andFlag3IsNotNull() {
            addCriterion("flag_3 is not null");
            return (Criteria) this;
        }

        public Criteria andFlag3EqualTo(Boolean value) {
            addCriterion("flag_3 =", value, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3NotEqualTo(Boolean value) {
            addCriterion("flag_3 <>", value, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3GreaterThan(Boolean value) {
            addCriterion("flag_3 >", value, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3GreaterThanOrEqualTo(Boolean value) {
            addCriterion("flag_3 >=", value, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3LessThan(Boolean value) {
            addCriterion("flag_3 <", value, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3LessThanOrEqualTo(Boolean value) {
            addCriterion("flag_3 <=", value, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3In(List<Boolean> values) {
            addCriterion("flag_3 in", values, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3NotIn(List<Boolean> values) {
            addCriterion("flag_3 not in", values, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3Between(Boolean value1, Boolean value2) {
            addCriterion("flag_3 between", value1, value2, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag3NotBetween(Boolean value1, Boolean value2) {
            addCriterion("flag_3 not between", value1, value2, "flag3");
            return (Criteria) this;
        }

        public Criteria andFlag4IsNull() {
            addCriterion("flag_4 is null");
            return (Criteria) this;
        }

        public Criteria andFlag4IsNotNull() {
            addCriterion("flag_4 is not null");
            return (Criteria) this;
        }

        public Criteria andFlag4EqualTo(Boolean value) {
            addCriterion("flag_4 =", value, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4NotEqualTo(Boolean value) {
            addCriterion("flag_4 <>", value, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4GreaterThan(Boolean value) {
            addCriterion("flag_4 >", value, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4GreaterThanOrEqualTo(Boolean value) {
            addCriterion("flag_4 >=", value, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4LessThan(Boolean value) {
            addCriterion("flag_4 <", value, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4LessThanOrEqualTo(Boolean value) {
            addCriterion("flag_4 <=", value, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4In(List<Boolean> values) {
            addCriterion("flag_4 in", values, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4NotIn(List<Boolean> values) {
            addCriterion("flag_4 not in", values, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4Between(Boolean value1, Boolean value2) {
            addCriterion("flag_4 between", value1, value2, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag4NotBetween(Boolean value1, Boolean value2) {
            addCriterion("flag_4 not between", value1, value2, "flag4");
            return (Criteria) this;
        }

        public Criteria andFlag5IsNull() {
            addCriterion("flag_5 is null");
            return (Criteria) this;
        }

        public Criteria andFlag5IsNotNull() {
            addCriterion("flag_5 is not null");
            return (Criteria) this;
        }

        public Criteria andFlag5EqualTo(Boolean value) {
            addCriterion("flag_5 =", value, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5NotEqualTo(Boolean value) {
            addCriterion("flag_5 <>", value, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5GreaterThan(Boolean value) {
            addCriterion("flag_5 >", value, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5GreaterThanOrEqualTo(Boolean value) {
            addCriterion("flag_5 >=", value, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5LessThan(Boolean value) {
            addCriterion("flag_5 <", value, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5LessThanOrEqualTo(Boolean value) {
            addCriterion("flag_5 <=", value, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5In(List<Boolean> values) {
            addCriterion("flag_5 in", values, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5NotIn(List<Boolean> values) {
            addCriterion("flag_5 not in", values, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5Between(Boolean value1, Boolean value2) {
            addCriterion("flag_5 between", value1, value2, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag5NotBetween(Boolean value1, Boolean value2) {
            addCriterion("flag_5 not between", value1, value2, "flag5");
            return (Criteria) this;
        }

        public Criteria andFlag6IsNull() {
            addCriterion("flag_6 is null");
            return (Criteria) this;
        }

        public Criteria andFlag6IsNotNull() {
            addCriterion("flag_6 is not null");
            return (Criteria) this;
        }

        public Criteria andFlag6EqualTo(Boolean value) {
            addCriterion("flag_6 =", value, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6NotEqualTo(Boolean value) {
            addCriterion("flag_6 <>", value, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6GreaterThan(Boolean value) {
            addCriterion("flag_6 >", value, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6GreaterThanOrEqualTo(Boolean value) {
            addCriterion("flag_6 >=", value, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6LessThan(Boolean value) {
            addCriterion("flag_6 <", value, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6LessThanOrEqualTo(Boolean value) {
            addCriterion("flag_6 <=", value, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6In(List<Boolean> values) {
            addCriterion("flag_6 in", values, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6NotIn(List<Boolean> values) {
            addCriterion("flag_6 not in", values, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6Between(Boolean value1, Boolean value2) {
            addCriterion("flag_6 between", value1, value2, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag6NotBetween(Boolean value1, Boolean value2) {
            addCriterion("flag_6 not between", value1, value2, "flag6");
            return (Criteria) this;
        }

        public Criteria andFlag7IsNull() {
            addCriterion("flag_7 is null");
            return (Criteria) this;
        }

        public Criteria andFlag7IsNotNull() {
            addCriterion("flag_7 is not null");
            return (Criteria) this;
        }

        public Criteria andFlag7EqualTo(Boolean value) {
            addCriterion("flag_7 =", value, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7NotEqualTo(Boolean value) {
            addCriterion("flag_7 <>", value, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7GreaterThan(Boolean value) {
            addCriterion("flag_7 >", value, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7GreaterThanOrEqualTo(Boolean value) {
            addCriterion("flag_7 >=", value, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7LessThan(Boolean value) {
            addCriterion("flag_7 <", value, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7LessThanOrEqualTo(Boolean value) {
            addCriterion("flag_7 <=", value, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7In(List<Boolean> values) {
            addCriterion("flag_7 in", values, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7NotIn(List<Boolean> values) {
            addCriterion("flag_7 not in", values, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7Between(Boolean value1, Boolean value2) {
            addCriterion("flag_7 between", value1, value2, "flag7");
            return (Criteria) this;
        }

        public Criteria andFlag7NotBetween(Boolean value1, Boolean value2) {
            addCriterion("flag_7 not between", value1, value2, "flag7");
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

    public static class Criteria extends BaseCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * goods_schedule_detail_lunch 2018-12-18
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