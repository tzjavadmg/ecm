package com.mili.oss.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderGoodsComboExample {
    /**
     * order_goods_combo
     */
    protected String orderByClause;

    /**
     * order_goods_combo
     */
    protected boolean distinct;

    /**
     * order_goods_combo
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public OrderGoodsComboExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     *
     * @mbg.generated 2019-03-07
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
     * @mbg.generated 2019-03-07
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     *
     * @mbg.generated 2019-03-07
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * order_goods_combo 2019-03-07
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

        public Criteria andOrderIdIsNull() {
            addCriterion("`order_id` is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("`order_id` is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(Long value) {
            addCriterion("`order_id` =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(Long value) {
            addCriterion("`order_id` <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(Long value) {
            addCriterion("`order_id` >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`order_id` >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(Long value) {
            addCriterion("`order_id` <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("`order_id` <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<Long> values) {
            addCriterion("`order_id` in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<Long> values) {
            addCriterion("`order_id` not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(Long value1, Long value2) {
            addCriterion("`order_id` between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("`order_id` not between", value1, value2, "orderId");
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

        public Criteria andGoodsCodeIsNull() {
            addCriterion("`goods_code` is null");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIsNotNull() {
            addCriterion("`goods_code` is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeEqualTo(String value) {
            addCriterion("`goods_code` =", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotEqualTo(String value) {
            addCriterion("`goods_code` <>", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeGreaterThan(String value) {
            addCriterion("`goods_code` >", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeGreaterThanOrEqualTo(String value) {
            addCriterion("`goods_code` >=", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLessThan(String value) {
            addCriterion("`goods_code` <", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLessThanOrEqualTo(String value) {
            addCriterion("`goods_code` <=", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLike(String value) {
            addCriterion("`goods_code` like", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotLike(String value) {
            addCriterion("`goods_code` not like", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIn(List<String> values) {
            addCriterion("`goods_code` in", values, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotIn(List<String> values) {
            addCriterion("`goods_code` not in", values, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeBetween(String value1, String value2) {
            addCriterion("`goods_code` between", value1, value2, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotBetween(String value1, String value2) {
            addCriterion("`goods_code` not between", value1, value2, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNull() {
            addCriterion("`goods_name` is null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNotNull() {
            addCriterion("`goods_name` is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameEqualTo(String value) {
            addCriterion("`goods_name` =", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotEqualTo(String value) {
            addCriterion("`goods_name` <>", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThan(String value) {
            addCriterion("`goods_name` >", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThanOrEqualTo(String value) {
            addCriterion("`goods_name` >=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThan(String value) {
            addCriterion("`goods_name` <", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThanOrEqualTo(String value) {
            addCriterion("`goods_name` <=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLike(String value) {
            addCriterion("`goods_name` like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotLike(String value) {
            addCriterion("`goods_name` not like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIn(List<String> values) {
            addCriterion("`goods_name` in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotIn(List<String> values) {
            addCriterion("`goods_name` not in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameBetween(String value1, String value2) {
            addCriterion("`goods_name` between", value1, value2, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotBetween(String value1, String value2) {
            addCriterion("`goods_name` not between", value1, value2, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIsNull() {
            addCriterion("`goods_count` is null");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIsNotNull() {
            addCriterion("`goods_count` is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsCountEqualTo(Integer value) {
            addCriterion("`goods_count` =", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotEqualTo(Integer value) {
            addCriterion("`goods_count` <>", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountGreaterThan(Integer value) {
            addCriterion("`goods_count` >", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("`goods_count` >=", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountLessThan(Integer value) {
            addCriterion("`goods_count` <", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountLessThanOrEqualTo(Integer value) {
            addCriterion("`goods_count` <=", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIn(List<Integer> values) {
            addCriterion("`goods_count` in", values, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotIn(List<Integer> values) {
            addCriterion("`goods_count` not in", values, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountBetween(Integer value1, Integer value2) {
            addCriterion("`goods_count` between", value1, value2, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotBetween(Integer value1, Integer value2) {
            addCriterion("`goods_count` not between", value1, value2, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeIsNull() {
            addCriterion("`combo_goods_code` is null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeIsNotNull() {
            addCriterion("`combo_goods_code` is not null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeEqualTo(String value) {
            addCriterion("`combo_goods_code` =", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotEqualTo(String value) {
            addCriterion("`combo_goods_code` <>", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeGreaterThan(String value) {
            addCriterion("`combo_goods_code` >", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeGreaterThanOrEqualTo(String value) {
            addCriterion("`combo_goods_code` >=", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeLessThan(String value) {
            addCriterion("`combo_goods_code` <", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeLessThanOrEqualTo(String value) {
            addCriterion("`combo_goods_code` <=", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeLike(String value) {
            addCriterion("`combo_goods_code` like", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotLike(String value) {
            addCriterion("`combo_goods_code` not like", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeIn(List<String> values) {
            addCriterion("`combo_goods_code` in", values, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotIn(List<String> values) {
            addCriterion("`combo_goods_code` not in", values, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeBetween(String value1, String value2) {
            addCriterion("`combo_goods_code` between", value1, value2, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotBetween(String value1, String value2) {
            addCriterion("`combo_goods_code` not between", value1, value2, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameIsNull() {
            addCriterion("`combo_goods_name` is null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameIsNotNull() {
            addCriterion("`combo_goods_name` is not null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameEqualTo(String value) {
            addCriterion("`combo_goods_name` =", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotEqualTo(String value) {
            addCriterion("`combo_goods_name` <>", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameGreaterThan(String value) {
            addCriterion("`combo_goods_name` >", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameGreaterThanOrEqualTo(String value) {
            addCriterion("`combo_goods_name` >=", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameLessThan(String value) {
            addCriterion("`combo_goods_name` <", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameLessThanOrEqualTo(String value) {
            addCriterion("`combo_goods_name` <=", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameLike(String value) {
            addCriterion("`combo_goods_name` like", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotLike(String value) {
            addCriterion("`combo_goods_name` not like", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameIn(List<String> values) {
            addCriterion("`combo_goods_name` in", values, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotIn(List<String> values) {
            addCriterion("`combo_goods_name` not in", values, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameBetween(String value1, String value2) {
            addCriterion("`combo_goods_name` between", value1, value2, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotBetween(String value1, String value2) {
            addCriterion("`combo_goods_name` not between", value1, value2, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountIsNull() {
            addCriterion("`combo_goods_count` is null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountIsNotNull() {
            addCriterion("`combo_goods_count` is not null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountEqualTo(Integer value) {
            addCriterion("`combo_goods_count` =", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountNotEqualTo(Integer value) {
            addCriterion("`combo_goods_count` <>", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountGreaterThan(Integer value) {
            addCriterion("`combo_goods_count` >", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("`combo_goods_count` >=", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountLessThan(Integer value) {
            addCriterion("`combo_goods_count` <", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountLessThanOrEqualTo(Integer value) {
            addCriterion("`combo_goods_count` <=", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountIn(List<Integer> values) {
            addCriterion("`combo_goods_count` in", values, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountNotIn(List<Integer> values) {
            addCriterion("`combo_goods_count` not in", values, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountBetween(Integer value1, Integer value2) {
            addCriterion("`combo_goods_count` between", value1, value2, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountNotBetween(Integer value1, Integer value2) {
            addCriterion("`combo_goods_count` not between", value1, value2, "comboGoodsCount");
            return (Criteria) this;
        }
    }

    public static class Criteria extends BaseCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * order_goods_combo 2019-03-07
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