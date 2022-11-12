package com.milisong.pos.production.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PreProductionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PreProductionExample() {
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

        public Criteria andPreProductionDateIsNull() {
            addCriterion("pre_production_date is null");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateIsNotNull() {
            addCriterion("pre_production_date is not null");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateEqualTo(Date value) {
            addCriterionForJDBCDate("pre_production_date =", value, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("pre_production_date <>", value, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateGreaterThan(Date value) {
            addCriterionForJDBCDate("pre_production_date >", value, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("pre_production_date >=", value, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateLessThan(Date value) {
            addCriterionForJDBCDate("pre_production_date <", value, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("pre_production_date <=", value, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateIn(List<Date> values) {
            addCriterionForJDBCDate("pre_production_date in", values, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("pre_production_date not in", values, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("pre_production_date between", value1, value2, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("pre_production_date not between", value1, value2, "preProductionDate");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountIsNull() {
            addCriterion("pre_production_count is null");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountIsNotNull() {
            addCriterion("pre_production_count is not null");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountEqualTo(BigDecimal value) {
            addCriterion("pre_production_count =", value, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountNotEqualTo(BigDecimal value) {
            addCriterion("pre_production_count <>", value, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountGreaterThan(BigDecimal value) {
            addCriterion("pre_production_count >", value, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pre_production_count >=", value, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountLessThan(BigDecimal value) {
            addCriterion("pre_production_count <", value, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pre_production_count <=", value, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountIn(List<BigDecimal> values) {
            addCriterion("pre_production_count in", values, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountNotIn(List<BigDecimal> values) {
            addCriterion("pre_production_count not in", values, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pre_production_count between", value1, value2, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andPreProductionCountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pre_production_count not between", value1, value2, "preProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountIsNull() {
            addCriterion("actual_production_count is null");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountIsNotNull() {
            addCriterion("actual_production_count is not null");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountEqualTo(Integer value) {
            addCriterion("actual_production_count =", value, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountNotEqualTo(Integer value) {
            addCriterion("actual_production_count <>", value, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountGreaterThan(Integer value) {
            addCriterion("actual_production_count >", value, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("actual_production_count >=", value, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountLessThan(Integer value) {
            addCriterion("actual_production_count <", value, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountLessThanOrEqualTo(Integer value) {
            addCriterion("actual_production_count <=", value, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountIn(List<Integer> values) {
            addCriterion("actual_production_count in", values, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountNotIn(List<Integer> values) {
            addCriterion("actual_production_count not in", values, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountBetween(Integer value1, Integer value2) {
            addCriterion("actual_production_count between", value1, value2, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualProductionCountNotBetween(Integer value1, Integer value2) {
            addCriterion("actual_production_count not between", value1, value2, "actualProductionCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountIsNull() {
            addCriterion("actual_sale_count is null");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountIsNotNull() {
            addCriterion("actual_sale_count is not null");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountEqualTo(Integer value) {
            addCriterion("actual_sale_count =", value, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountNotEqualTo(Integer value) {
            addCriterion("actual_sale_count <>", value, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountGreaterThan(Integer value) {
            addCriterion("actual_sale_count >", value, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("actual_sale_count >=", value, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountLessThan(Integer value) {
            addCriterion("actual_sale_count <", value, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountLessThanOrEqualTo(Integer value) {
            addCriterion("actual_sale_count <=", value, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountIn(List<Integer> values) {
            addCriterion("actual_sale_count in", values, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountNotIn(List<Integer> values) {
            addCriterion("actual_sale_count not in", values, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountBetween(Integer value1, Integer value2) {
            addCriterion("actual_sale_count between", value1, value2, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andActualSaleCountNotBetween(Integer value1, Integer value2) {
            addCriterion("actual_sale_count not between", value1, value2, "actualSaleCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountIsNull() {
            addCriterion("sales_average_count is null");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountIsNotNull() {
            addCriterion("sales_average_count is not null");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountEqualTo(BigDecimal value) {
            addCriterion("sales_average_count =", value, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountNotEqualTo(BigDecimal value) {
            addCriterion("sales_average_count <>", value, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountGreaterThan(BigDecimal value) {
            addCriterion("sales_average_count >", value, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sales_average_count >=", value, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountLessThan(BigDecimal value) {
            addCriterion("sales_average_count <", value, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sales_average_count <=", value, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountIn(List<BigDecimal> values) {
            addCriterion("sales_average_count in", values, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountNotIn(List<BigDecimal> values) {
            addCriterion("sales_average_count not in", values, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sales_average_count between", value1, value2, "salesAverageCount");
            return (Criteria) this;
        }

        public Criteria andSalesAverageCountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sales_average_count not between", value1, value2, "salesAverageCount");
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