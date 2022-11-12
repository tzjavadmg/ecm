package com.milisong.breakfast.scm.orderset.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderSetDetailGoodsExample {
    /**
     * order_set_detail_goods
     */
    protected String orderByClause;

    /**
     * order_set_detail_goods
     */
    protected boolean distinct;

    /**
     * order_set_detail_goods
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public OrderSetDetailGoodsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-19
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
     * @mbg.generated 2018-12-19
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * order_set_detail_goods 2018-12-19
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

        public Criteria andOrderNoIsNull() {
            addCriterion("order_no is null");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNotNull() {
            addCriterion("order_no is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNoEqualTo(String value) {
            addCriterion("order_no =", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotEqualTo(String value) {
            addCriterion("order_no <>", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThan(String value) {
            addCriterion("order_no >", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("order_no >=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThan(String value) {
            addCriterion("order_no <", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThanOrEqualTo(String value) {
            addCriterion("order_no <=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLike(String value) {
            addCriterion("order_no like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotLike(String value) {
            addCriterion("order_no not like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoIn(List<String> values) {
            addCriterion("order_no in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotIn(List<String> values) {
            addCriterion("order_no not in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoBetween(String value1, String value2) {
            addCriterion("order_no between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotBetween(String value1, String value2) {
            addCriterion("order_no not between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoIsNull() {
            addCriterion("couplet_no is null");
            return (Criteria) this;
        }

        public Criteria andCoupletNoIsNotNull() {
            addCriterion("couplet_no is not null");
            return (Criteria) this;
        }

        public Criteria andCoupletNoEqualTo(String value) {
            addCriterion("couplet_no =", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoNotEqualTo(String value) {
            addCriterion("couplet_no <>", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoGreaterThan(String value) {
            addCriterion("couplet_no >", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoGreaterThanOrEqualTo(String value) {
            addCriterion("couplet_no >=", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoLessThan(String value) {
            addCriterion("couplet_no <", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoLessThanOrEqualTo(String value) {
            addCriterion("couplet_no <=", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoLike(String value) {
            addCriterion("couplet_no like", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoNotLike(String value) {
            addCriterion("couplet_no not like", value, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoIn(List<String> values) {
            addCriterion("couplet_no in", values, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoNotIn(List<String> values) {
            addCriterion("couplet_no not in", values, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoBetween(String value1, String value2) {
            addCriterion("couplet_no between", value1, value2, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andCoupletNoNotBetween(String value1, String value2) {
            addCriterion("couplet_no not between", value1, value2, "coupletNo");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserPhoneIsNull() {
            addCriterion("user_phone is null");
            return (Criteria) this;
        }

        public Criteria andUserPhoneIsNotNull() {
            addCriterion("user_phone is not null");
            return (Criteria) this;
        }

        public Criteria andUserPhoneEqualTo(String value) {
            addCriterion("user_phone =", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneNotEqualTo(String value) {
            addCriterion("user_phone <>", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneGreaterThan(String value) {
            addCriterion("user_phone >", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("user_phone >=", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneLessThan(String value) {
            addCriterion("user_phone <", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneLessThanOrEqualTo(String value) {
            addCriterion("user_phone <=", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneLike(String value) {
            addCriterion("user_phone like", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneNotLike(String value) {
            addCriterion("user_phone not like", value, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneIn(List<String> values) {
            addCriterion("user_phone in", values, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneNotIn(List<String> values) {
            addCriterion("user_phone not in", values, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneBetween(String value1, String value2) {
            addCriterion("user_phone between", value1, value2, "userPhone");
            return (Criteria) this;
        }

        public Criteria andUserPhoneNotBetween(String value1, String value2) {
            addCriterion("user_phone not between", value1, value2, "userPhone");
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

        public Criteria andGoodsNumberIsNull() {
            addCriterion("goods_number is null");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberIsNotNull() {
            addCriterion("goods_number is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberEqualTo(Integer value) {
            addCriterion("goods_number =", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberNotEqualTo(Integer value) {
            addCriterion("goods_number <>", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberGreaterThan(Integer value) {
            addCriterion("goods_number >", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_number >=", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberLessThan(Integer value) {
            addCriterion("goods_number <", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberLessThanOrEqualTo(Integer value) {
            addCriterion("goods_number <=", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberIn(List<Integer> values) {
            addCriterion("goods_number in", values, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberNotIn(List<Integer> values) {
            addCriterion("goods_number not in", values, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberBetween(Integer value1, Integer value2) {
            addCriterion("goods_number between", value1, value2, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_number not between", value1, value2, "goodsNumber");
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

        public Criteria andIsComboIsNull() {
            addCriterion("is_combo is null");
            return (Criteria) this;
        }

        public Criteria andIsComboIsNotNull() {
            addCriterion("is_combo is not null");
            return (Criteria) this;
        }

        public Criteria andIsComboEqualTo(Boolean value) {
            addCriterion("is_combo =", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboNotEqualTo(Boolean value) {
            addCriterion("is_combo <>", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboGreaterThan(Boolean value) {
            addCriterion("is_combo >", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_combo >=", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboLessThan(Boolean value) {
            addCriterion("is_combo <", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboLessThanOrEqualTo(Boolean value) {
            addCriterion("is_combo <=", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboIn(List<Boolean> values) {
            addCriterion("is_combo in", values, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboNotIn(List<Boolean> values) {
            addCriterion("is_combo not in", values, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboBetween(Boolean value1, Boolean value2) {
            addCriterion("is_combo between", value1, value2, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_combo not between", value1, value2, "isCombo");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeIsNull() {
            addCriterion("combo_goods_code is null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeIsNotNull() {
            addCriterion("combo_goods_code is not null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeEqualTo(String value) {
            addCriterion("combo_goods_code =", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotEqualTo(String value) {
            addCriterion("combo_goods_code <>", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeGreaterThan(String value) {
            addCriterion("combo_goods_code >", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeGreaterThanOrEqualTo(String value) {
            addCriterion("combo_goods_code >=", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeLessThan(String value) {
            addCriterion("combo_goods_code <", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeLessThanOrEqualTo(String value) {
            addCriterion("combo_goods_code <=", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeLike(String value) {
            addCriterion("combo_goods_code like", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotLike(String value) {
            addCriterion("combo_goods_code not like", value, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeIn(List<String> values) {
            addCriterion("combo_goods_code in", values, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotIn(List<String> values) {
            addCriterion("combo_goods_code not in", values, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeBetween(String value1, String value2) {
            addCriterion("combo_goods_code between", value1, value2, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCodeNotBetween(String value1, String value2) {
            addCriterion("combo_goods_code not between", value1, value2, "comboGoodsCode");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameIsNull() {
            addCriterion("combo_goods_name is null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameIsNotNull() {
            addCriterion("combo_goods_name is not null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameEqualTo(String value) {
            addCriterion("combo_goods_name =", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotEqualTo(String value) {
            addCriterion("combo_goods_name <>", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameGreaterThan(String value) {
            addCriterion("combo_goods_name >", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameGreaterThanOrEqualTo(String value) {
            addCriterion("combo_goods_name >=", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameLessThan(String value) {
            addCriterion("combo_goods_name <", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameLessThanOrEqualTo(String value) {
            addCriterion("combo_goods_name <=", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameLike(String value) {
            addCriterion("combo_goods_name like", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotLike(String value) {
            addCriterion("combo_goods_name not like", value, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameIn(List<String> values) {
            addCriterion("combo_goods_name in", values, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotIn(List<String> values) {
            addCriterion("combo_goods_name not in", values, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameBetween(String value1, String value2) {
            addCriterion("combo_goods_name between", value1, value2, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsNameNotBetween(String value1, String value2) {
            addCriterion("combo_goods_name not between", value1, value2, "comboGoodsName");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountIsNull() {
            addCriterion("combo_goods_count is null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountIsNotNull() {
            addCriterion("combo_goods_count is not null");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountEqualTo(Integer value) {
            addCriterion("combo_goods_count =", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountNotEqualTo(Integer value) {
            addCriterion("combo_goods_count <>", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountGreaterThan(Integer value) {
            addCriterion("combo_goods_count >", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("combo_goods_count >=", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountLessThan(Integer value) {
            addCriterion("combo_goods_count <", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountLessThanOrEqualTo(Integer value) {
            addCriterion("combo_goods_count <=", value, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountIn(List<Integer> values) {
            addCriterion("combo_goods_count in", values, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountNotIn(List<Integer> values) {
            addCriterion("combo_goods_count not in", values, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountBetween(Integer value1, Integer value2) {
            addCriterion("combo_goods_count between", value1, value2, "comboGoodsCount");
            return (Criteria) this;
        }

        public Criteria andComboGoodsCountNotBetween(Integer value1, Integer value2) {
            addCriterion("combo_goods_count not between", value1, value2, "comboGoodsCount");
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
     * order_set_detail_goods 2018-12-19
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