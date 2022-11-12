package com.milisong.breakfast.scm.order.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailExample {
    /**
     * order_detail
     */
    protected String orderByClause;

    /**
     * order_detail
     */
    protected boolean distinct;

    /**
     * order_detail
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2018-12-12
     */
    public OrderDetailExample() {
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
     * order_detail 2018-12-12
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

        public Criteria andGoodsPriceIsNull() {
            addCriterion("`goods_price` is null");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceIsNotNull() {
            addCriterion("`goods_price` is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceEqualTo(BigDecimal value) {
            addCriterion("`goods_price` =", value, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceNotEqualTo(BigDecimal value) {
            addCriterion("`goods_price` <>", value, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceGreaterThan(BigDecimal value) {
            addCriterion("`goods_price` >", value, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`goods_price` >=", value, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceLessThan(BigDecimal value) {
            addCriterion("`goods_price` <", value, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`goods_price` <=", value, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceIn(List<BigDecimal> values) {
            addCriterion("`goods_price` in", values, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceNotIn(List<BigDecimal> values) {
            addCriterion("`goods_price` not in", values, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`goods_price` between", value1, value2, "goodsPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`goods_price` not between", value1, value2, "goodsPrice");
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

        public Criteria andGoodsDiscountPriceIsNull() {
            addCriterion("`goods_discount_price` is null");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceIsNotNull() {
            addCriterion("`goods_discount_price` is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceEqualTo(BigDecimal value) {
            addCriterion("`goods_discount_price` =", value, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceNotEqualTo(BigDecimal value) {
            addCriterion("`goods_discount_price` <>", value, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceGreaterThan(BigDecimal value) {
            addCriterion("`goods_discount_price` >", value, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`goods_discount_price` >=", value, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceLessThan(BigDecimal value) {
            addCriterion("`goods_discount_price` <", value, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`goods_discount_price` <=", value, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceIn(List<BigDecimal> values) {
            addCriterion("`goods_discount_price` in", values, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceNotIn(List<BigDecimal> values) {
            addCriterion("`goods_discount_price` not in", values, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`goods_discount_price` between", value1, value2, "goodsDiscountPrice");
            return (Criteria) this;
        }

        public Criteria andGoodsDiscountPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`goods_discount_price` not between", value1, value2, "goodsDiscountPrice");
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

        public Criteria andIsComboIsNull() {
            addCriterion("`is_combo` is null");
            return (Criteria) this;
        }

        public Criteria andIsComboIsNotNull() {
            addCriterion("`is_combo` is not null");
            return (Criteria) this;
        }

        public Criteria andIsComboEqualTo(Boolean value) {
            addCriterion("`is_combo` =", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboNotEqualTo(Boolean value) {
            addCriterion("`is_combo` <>", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboGreaterThan(Boolean value) {
            addCriterion("`is_combo` >", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboGreaterThanOrEqualTo(Boolean value) {
            addCriterion("`is_combo` >=", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboLessThan(Boolean value) {
            addCriterion("`is_combo` <", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboLessThanOrEqualTo(Boolean value) {
            addCriterion("`is_combo` <=", value, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboIn(List<Boolean> values) {
            addCriterion("`is_combo` in", values, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboNotIn(List<Boolean> values) {
            addCriterion("`is_combo` not in", values, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboBetween(Boolean value1, Boolean value2) {
            addCriterion("`is_combo` between", value1, value2, "isCombo");
            return (Criteria) this;
        }

        public Criteria andIsComboNotBetween(Boolean value1, Boolean value2) {
            addCriterion("`is_combo` not between", value1, value2, "isCombo");
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
    }

    public static class Criteria extends BaseCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * order_detail 2018-12-12
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