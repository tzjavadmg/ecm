package com.milisong.scm.goods.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.milisong.scm.goods.common.param.PageInfo;

public class GoodsLunchExample extends PageInfo {
    /**
     * goods_lunch
     */
    protected String orderByClause;

    /**
     * goods_lunch
     */
    protected boolean distinct;

    /**
     * goods_lunch
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2018-12-19
     */
    public GoodsLunchExample() {
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
     * goods_lunch 2018-12-19
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

        public Criteria andCodeIsNull() {
            addCriterion("`code` is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("`code` is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("`code` =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("`code` <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("`code` >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("`code` >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("`code` <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("`code` <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("`code` like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("`code` not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("`code` in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("`code` not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("`code` between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("`code` not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeIsNull() {
            addCriterion("`category_code` is null");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeIsNotNull() {
            addCriterion("`category_code` is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeEqualTo(String value) {
            addCriterion("`category_code` =", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeNotEqualTo(String value) {
            addCriterion("`category_code` <>", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeGreaterThan(String value) {
            addCriterion("`category_code` >", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeGreaterThanOrEqualTo(String value) {
            addCriterion("`category_code` >=", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeLessThan(String value) {
            addCriterion("`category_code` <", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeLessThanOrEqualTo(String value) {
            addCriterion("`category_code` <=", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeLike(String value) {
            addCriterion("`category_code` like", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeNotLike(String value) {
            addCriterion("`category_code` not like", value, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeIn(List<String> values) {
            addCriterion("`category_code` in", values, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeNotIn(List<String> values) {
            addCriterion("`category_code` not in", values, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeBetween(String value1, String value2) {
            addCriterion("`category_code` between", value1, value2, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryCodeNotBetween(String value1, String value2) {
            addCriterion("`category_code` not between", value1, value2, "categoryCode");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIsNull() {
            addCriterion("`category_name` is null");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIsNotNull() {
            addCriterion("`category_name` is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryNameEqualTo(String value) {
            addCriterion("`category_name` =", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotEqualTo(String value) {
            addCriterion("`category_name` <>", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameGreaterThan(String value) {
            addCriterion("`category_name` >", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameGreaterThanOrEqualTo(String value) {
            addCriterion("`category_name` >=", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLessThan(String value) {
            addCriterion("`category_name` <", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLessThanOrEqualTo(String value) {
            addCriterion("`category_name` <=", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLike(String value) {
            addCriterion("`category_name` like", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotLike(String value) {
            addCriterion("`category_name` not like", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIn(List<String> values) {
            addCriterion("`category_name` in", values, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotIn(List<String> values) {
            addCriterion("`category_name` not in", values, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameBetween(String value1, String value2) {
            addCriterion("`category_name` between", value1, value2, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotBetween(String value1, String value2) {
            addCriterion("`category_name` not between", value1, value2, "categoryName");
            return (Criteria) this;
        }

        public Criteria andDescribeIsNull() {
            addCriterion("`describe` is null");
            return (Criteria) this;
        }

        public Criteria andDescribeIsNotNull() {
            addCriterion("`describe` is not null");
            return (Criteria) this;
        }

        public Criteria andDescribeEqualTo(String value) {
            addCriterion("`describe` =", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotEqualTo(String value) {
            addCriterion("`describe` <>", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeGreaterThan(String value) {
            addCriterion("`describe` >", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeGreaterThanOrEqualTo(String value) {
            addCriterion("`describe` >=", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeLessThan(String value) {
            addCriterion("`describe` <", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeLessThanOrEqualTo(String value) {
            addCriterion("`describe` <=", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeLike(String value) {
            addCriterion("`describe` like", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotLike(String value) {
            addCriterion("`describe` not like", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeIn(List<String> values) {
            addCriterion("`describe` in", values, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotIn(List<String> values) {
            addCriterion("`describe` not in", values, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeBetween(String value1, String value2) {
            addCriterion("`describe` between", value1, value2, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotBetween(String value1, String value2) {
            addCriterion("`describe` not between", value1, value2, "describe");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceIsNull() {
            addCriterion("`advise_price` is null");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceIsNotNull() {
            addCriterion("`advise_price` is not null");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceEqualTo(BigDecimal value) {
            addCriterion("`advise_price` =", value, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceNotEqualTo(BigDecimal value) {
            addCriterion("`advise_price` <>", value, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceGreaterThan(BigDecimal value) {
            addCriterion("`advise_price` >", value, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`advise_price` >=", value, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceLessThan(BigDecimal value) {
            addCriterion("`advise_price` <", value, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`advise_price` <=", value, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceIn(List<BigDecimal> values) {
            addCriterion("`advise_price` in", values, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceNotIn(List<BigDecimal> values) {
            addCriterion("`advise_price` not in", values, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`advise_price` between", value1, value2, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andAdvisePriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`advise_price` not between", value1, value2, "advisePrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceIsNull() {
            addCriterion("`preferential_price` is null");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceIsNotNull() {
            addCriterion("`preferential_price` is not null");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceEqualTo(BigDecimal value) {
            addCriterion("`preferential_price` =", value, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceNotEqualTo(BigDecimal value) {
            addCriterion("`preferential_price` <>", value, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceGreaterThan(BigDecimal value) {
            addCriterion("`preferential_price` >", value, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`preferential_price` >=", value, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceLessThan(BigDecimal value) {
            addCriterion("`preferential_price` <", value, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`preferential_price` <=", value, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceIn(List<BigDecimal> values) {
            addCriterion("`preferential_price` in", values, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceNotIn(List<BigDecimal> values) {
            addCriterion("`preferential_price` not in", values, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`preferential_price` between", value1, value2, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andPreferentialPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`preferential_price` not between", value1, value2, "preferentialPrice");
            return (Criteria) this;
        }

        public Criteria andDiscountIsNull() {
            addCriterion("`discount` is null");
            return (Criteria) this;
        }

        public Criteria andDiscountIsNotNull() {
            addCriterion("`discount` is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountEqualTo(BigDecimal value) {
            addCriterion("`discount` =", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotEqualTo(BigDecimal value) {
            addCriterion("`discount` <>", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThan(BigDecimal value) {
            addCriterion("`discount` >", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`discount` >=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThan(BigDecimal value) {
            addCriterion("`discount` <", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`discount` <=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountIn(List<BigDecimal> values) {
            addCriterion("`discount` in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotIn(List<BigDecimal> values) {
            addCriterion("`discount` not in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`discount` between", value1, value2, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`discount` not between", value1, value2, "discount");
            return (Criteria) this;
        }

        public Criteria andPictureIsNull() {
            addCriterion("`picture` is null");
            return (Criteria) this;
        }

        public Criteria andPictureIsNotNull() {
            addCriterion("`picture` is not null");
            return (Criteria) this;
        }

        public Criteria andPictureEqualTo(String value) {
            addCriterion("`picture` =", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureNotEqualTo(String value) {
            addCriterion("`picture` <>", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureGreaterThan(String value) {
            addCriterion("`picture` >", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureGreaterThanOrEqualTo(String value) {
            addCriterion("`picture` >=", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureLessThan(String value) {
            addCriterion("`picture` <", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureLessThanOrEqualTo(String value) {
            addCriterion("`picture` <=", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureLike(String value) {
            addCriterion("`picture` like", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureNotLike(String value) {
            addCriterion("`picture` not like", value, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureIn(List<String> values) {
            addCriterion("`picture` in", values, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureNotIn(List<String> values) {
            addCriterion("`picture` not in", values, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureBetween(String value1, String value2) {
            addCriterion("`picture` between", value1, value2, "picture");
            return (Criteria) this;
        }

        public Criteria andPictureNotBetween(String value1, String value2) {
            addCriterion("`picture` not between", value1, value2, "picture");
            return (Criteria) this;
        }

        public Criteria andBigPictureIsNull() {
            addCriterion("`big_picture` is null");
            return (Criteria) this;
        }

        public Criteria andBigPictureIsNotNull() {
            addCriterion("`big_picture` is not null");
            return (Criteria) this;
        }

        public Criteria andBigPictureEqualTo(String value) {
            addCriterion("`big_picture` =", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureNotEqualTo(String value) {
            addCriterion("`big_picture` <>", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureGreaterThan(String value) {
            addCriterion("`big_picture` >", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureGreaterThanOrEqualTo(String value) {
            addCriterion("`big_picture` >=", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureLessThan(String value) {
            addCriterion("`big_picture` <", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureLessThanOrEqualTo(String value) {
            addCriterion("`big_picture` <=", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureLike(String value) {
            addCriterion("`big_picture` like", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureNotLike(String value) {
            addCriterion("`big_picture` not like", value, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureIn(List<String> values) {
            addCriterion("`big_picture` in", values, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureNotIn(List<String> values) {
            addCriterion("`big_picture` not in", values, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureBetween(String value1, String value2) {
            addCriterion("`big_picture` between", value1, value2, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andBigPictureNotBetween(String value1, String value2) {
            addCriterion("`big_picture` not between", value1, value2, "bigPicture");
            return (Criteria) this;
        }

        public Criteria andSpicyIsNull() {
            addCriterion("`spicy` is null");
            return (Criteria) this;
        }

        public Criteria andSpicyIsNotNull() {
            addCriterion("`spicy` is not null");
            return (Criteria) this;
        }

        public Criteria andSpicyEqualTo(Byte value) {
            addCriterion("`spicy` =", value, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyNotEqualTo(Byte value) {
            addCriterion("`spicy` <>", value, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyGreaterThan(Byte value) {
            addCriterion("`spicy` >", value, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyGreaterThanOrEqualTo(Byte value) {
            addCriterion("`spicy` >=", value, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyLessThan(Byte value) {
            addCriterion("`spicy` <", value, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyLessThanOrEqualTo(Byte value) {
            addCriterion("`spicy` <=", value, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyIn(List<Byte> values) {
            addCriterion("`spicy` in", values, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyNotIn(List<Byte> values) {
            addCriterion("`spicy` not in", values, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyBetween(Byte value1, Byte value2) {
            addCriterion("`spicy` between", value1, value2, "spicy");
            return (Criteria) this;
        }

        public Criteria andSpicyNotBetween(Byte value1, Byte value2) {
            addCriterion("`spicy` not between", value1, value2, "spicy");
            return (Criteria) this;
        }

        public Criteria andTasteIsNull() {
            addCriterion("`taste` is null");
            return (Criteria) this;
        }

        public Criteria andTasteIsNotNull() {
            addCriterion("`taste` is not null");
            return (Criteria) this;
        }

        public Criteria andTasteEqualTo(String value) {
            addCriterion("`taste` =", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteNotEqualTo(String value) {
            addCriterion("`taste` <>", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteGreaterThan(String value) {
            addCriterion("`taste` >", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteGreaterThanOrEqualTo(String value) {
            addCriterion("`taste` >=", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteLessThan(String value) {
            addCriterion("`taste` <", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteLessThanOrEqualTo(String value) {
            addCriterion("`taste` <=", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteLike(String value) {
            addCriterion("`taste` like", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteNotLike(String value) {
            addCriterion("`taste` not like", value, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteIn(List<String> values) {
            addCriterion("`taste` in", values, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteNotIn(List<String> values) {
            addCriterion("`taste` not in", values, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteBetween(String value1, String value2) {
            addCriterion("`taste` between", value1, value2, "taste");
            return (Criteria) this;
        }

        public Criteria andTasteNotBetween(String value1, String value2) {
            addCriterion("`taste` not between", value1, value2, "taste");
            return (Criteria) this;
        }

        public Criteria andWeightIsNull() {
            addCriterion("`weight` is null");
            return (Criteria) this;
        }

        public Criteria andWeightIsNotNull() {
            addCriterion("`weight` is not null");
            return (Criteria) this;
        }

        public Criteria andWeightEqualTo(Byte value) {
            addCriterion("`weight` =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(Byte value) {
            addCriterion("`weight` <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(Byte value) {
            addCriterion("`weight` >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(Byte value) {
            addCriterion("`weight` >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(Byte value) {
            addCriterion("`weight` <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(Byte value) {
            addCriterion("`weight` <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<Byte> values) {
            addCriterion("`weight` in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<Byte> values) {
            addCriterion("`weight` not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(Byte value1, Byte value2) {
            addCriterion("`weight` between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(Byte value1, Byte value2) {
            addCriterion("`weight` not between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("`is_deleted` is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("`is_deleted` is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Boolean value) {
            addCriterion("`is_deleted` =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Boolean value) {
            addCriterion("`is_deleted` <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Boolean value) {
            addCriterion("`is_deleted` >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("`is_deleted` >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Boolean value) {
            addCriterion("`is_deleted` <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("`is_deleted` <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Boolean> values) {
            addCriterion("`is_deleted` in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Boolean> values) {
            addCriterion("`is_deleted` not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("`is_deleted` between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("`is_deleted` not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNull() {
            addCriterion("`create_by` is null");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNotNull() {
            addCriterion("`create_by` is not null");
            return (Criteria) this;
        }

        public Criteria andCreateByEqualTo(String value) {
            addCriterion("`create_by` =", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotEqualTo(String value) {
            addCriterion("`create_by` <>", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThan(String value) {
            addCriterion("`create_by` >", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThanOrEqualTo(String value) {
            addCriterion("`create_by` >=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThan(String value) {
            addCriterion("`create_by` <", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThanOrEqualTo(String value) {
            addCriterion("`create_by` <=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLike(String value) {
            addCriterion("`create_by` like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotLike(String value) {
            addCriterion("`create_by` not like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByIn(List<String> values) {
            addCriterion("`create_by` in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotIn(List<String> values) {
            addCriterion("`create_by` not in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByBetween(String value1, String value2) {
            addCriterion("`create_by` between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotBetween(String value1, String value2) {
            addCriterion("`create_by` not between", value1, value2, "createBy");
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

        public Criteria andUpdateByIsNull() {
            addCriterion("`update_by` is null");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNotNull() {
            addCriterion("`update_by` is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateByEqualTo(String value) {
            addCriterion("`update_by` =", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotEqualTo(String value) {
            addCriterion("`update_by` <>", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThan(String value) {
            addCriterion("`update_by` >", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
            addCriterion("`update_by` >=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThan(String value) {
            addCriterion("`update_by` <", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThanOrEqualTo(String value) {
            addCriterion("`update_by` <=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLike(String value) {
            addCriterion("`update_by` like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotLike(String value) {
            addCriterion("`update_by` not like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByIn(List<String> values) {
            addCriterion("`update_by` in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotIn(List<String> values) {
            addCriterion("`update_by` not in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByBetween(String value1, String value2) {
            addCriterion("`update_by` between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotBetween(String value1, String value2) {
            addCriterion("`update_by` not between", value1, value2, "updateBy");
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
     * goods_lunch 2018-12-19
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