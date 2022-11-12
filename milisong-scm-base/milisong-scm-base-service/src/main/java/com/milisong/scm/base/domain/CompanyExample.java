package com.milisong.scm.base.domain;

import com.milisong.scm.base.dto.param.PageInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CompanyExample extends PageInfo {
    /**
     * company
     */
    protected String orderByClause;

    /**
     * company
     */
    protected boolean distinct;

    /**
     * company
     */
    protected List<Criteria> oredCriteria;

    /**
     * @mbg.generated 2018-12-03
     */
    public CompanyExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * @mbg.generated 2018-12-03
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * company 2018-12-03
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andBuildingNameIsNull() {
            addCriterion("building_name is null");
            return (Criteria) this;
        }

        public Criteria andBuildingNameIsNotNull() {
            addCriterion("building_name is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingNameEqualTo(String value) {
            addCriterion("building_name =", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameNotEqualTo(String value) {
            addCriterion("building_name <>", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameGreaterThan(String value) {
            addCriterion("building_name >", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameGreaterThanOrEqualTo(String value) {
            addCriterion("building_name >=", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameLessThan(String value) {
            addCriterion("building_name <", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameLessThanOrEqualTo(String value) {
            addCriterion("building_name <=", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameLike(String value) {
            addCriterion("building_name like", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameNotLike(String value) {
            addCriterion("building_name not like", value, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameIn(List<String> values) {
            addCriterion("building_name in", values, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameNotIn(List<String> values) {
            addCriterion("building_name not in", values, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameBetween(String value1, String value2) {
            addCriterion("building_name between", value1, value2, "buildingName");
            return (Criteria) this;
        }

        public Criteria andBuildingNameNotBetween(String value1, String value2) {
            addCriterion("building_name not between", value1, value2, "buildingName");
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

        public Criteria andCityCodeIsNull() {
            addCriterion("city_code is null");
            return (Criteria) this;
        }

        public Criteria andCityCodeIsNotNull() {
            addCriterion("city_code is not null");
            return (Criteria) this;
        }

        public Criteria andCityCodeEqualTo(String value) {
            addCriterion("city_code =", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotEqualTo(String value) {
            addCriterion("city_code <>", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeGreaterThan(String value) {
            addCriterion("city_code >", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeGreaterThanOrEqualTo(String value) {
            addCriterion("city_code >=", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLessThan(String value) {
            addCriterion("city_code <", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLessThanOrEqualTo(String value) {
            addCriterion("city_code <=", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLike(String value) {
            addCriterion("city_code like", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotLike(String value) {
            addCriterion("city_code not like", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeIn(List<String> values) {
            addCriterion("city_code in", values, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotIn(List<String> values) {
            addCriterion("city_code not in", values, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeBetween(String value1, String value2) {
            addCriterion("city_code between", value1, value2, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotBetween(String value1, String value2) {
            addCriterion("city_code not between", value1, value2, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityNameIsNull() {
            addCriterion("city_name is null");
            return (Criteria) this;
        }

        public Criteria andCityNameIsNotNull() {
            addCriterion("city_name is not null");
            return (Criteria) this;
        }

        public Criteria andCityNameEqualTo(String value) {
            addCriterion("city_name =", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotEqualTo(String value) {
            addCriterion("city_name <>", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameGreaterThan(String value) {
            addCriterion("city_name >", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameGreaterThanOrEqualTo(String value) {
            addCriterion("city_name >=", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameLessThan(String value) {
            addCriterion("city_name <", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameLessThanOrEqualTo(String value) {
            addCriterion("city_name <=", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameLike(String value) {
            addCriterion("city_name like", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotLike(String value) {
            addCriterion("city_name not like", value, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameIn(List<String> values) {
            addCriterion("city_name in", values, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotIn(List<String> values) {
            addCriterion("city_name not in", values, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameBetween(String value1, String value2) {
            addCriterion("city_name between", value1, value2, "cityName");
            return (Criteria) this;
        }

        public Criteria andCityNameNotBetween(String value1, String value2) {
            addCriterion("city_name not between", value1, value2, "cityName");
            return (Criteria) this;
        }

        public Criteria andRegionCodeIsNull() {
            addCriterion("region_code is null");
            return (Criteria) this;
        }

        public Criteria andRegionCodeIsNotNull() {
            addCriterion("region_code is not null");
            return (Criteria) this;
        }

        public Criteria andRegionCodeEqualTo(String value) {
            addCriterion("region_code =", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeNotEqualTo(String value) {
            addCriterion("region_code <>", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeGreaterThan(String value) {
            addCriterion("region_code >", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeGreaterThanOrEqualTo(String value) {
            addCriterion("region_code >=", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeLessThan(String value) {
            addCriterion("region_code <", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeLessThanOrEqualTo(String value) {
            addCriterion("region_code <=", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeLike(String value) {
            addCriterion("region_code like", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeNotLike(String value) {
            addCriterion("region_code not like", value, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeIn(List<String> values) {
            addCriterion("region_code in", values, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeNotIn(List<String> values) {
            addCriterion("region_code not in", values, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeBetween(String value1, String value2) {
            addCriterion("region_code between", value1, value2, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionCodeNotBetween(String value1, String value2) {
            addCriterion("region_code not between", value1, value2, "regionCode");
            return (Criteria) this;
        }

        public Criteria andRegionNameIsNull() {
            addCriterion("region_name is null");
            return (Criteria) this;
        }

        public Criteria andRegionNameIsNotNull() {
            addCriterion("region_name is not null");
            return (Criteria) this;
        }

        public Criteria andRegionNameEqualTo(String value) {
            addCriterion("region_name =", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameNotEqualTo(String value) {
            addCriterion("region_name <>", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameGreaterThan(String value) {
            addCriterion("region_name >", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameGreaterThanOrEqualTo(String value) {
            addCriterion("region_name >=", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameLessThan(String value) {
            addCriterion("region_name <", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameLessThanOrEqualTo(String value) {
            addCriterion("region_name <=", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameLike(String value) {
            addCriterion("region_name like", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameNotLike(String value) {
            addCriterion("region_name not like", value, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameIn(List<String> values) {
            addCriterion("region_name in", values, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameNotIn(List<String> values) {
            addCriterion("region_name not in", values, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameBetween(String value1, String value2) {
            addCriterion("region_name between", value1, value2, "regionName");
            return (Criteria) this;
        }

        public Criteria andRegionNameNotBetween(String value1, String value2) {
            addCriterion("region_name not between", value1, value2, "regionName");
            return (Criteria) this;
        }

        public Criteria andDetailAddressIsNull() {
            addCriterion("detail_address is null");
            return (Criteria) this;
        }

        public Criteria andDetailAddressIsNotNull() {
            addCriterion("detail_address is not null");
            return (Criteria) this;
        }

        public Criteria andDetailAddressEqualTo(String value) {
            addCriterion("detail_address =", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressNotEqualTo(String value) {
            addCriterion("detail_address <>", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressGreaterThan(String value) {
            addCriterion("detail_address >", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressGreaterThanOrEqualTo(String value) {
            addCriterion("detail_address >=", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressLessThan(String value) {
            addCriterion("detail_address <", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressLessThanOrEqualTo(String value) {
            addCriterion("detail_address <=", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressLike(String value) {
            addCriterion("detail_address like", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressNotLike(String value) {
            addCriterion("detail_address not like", value, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressIn(List<String> values) {
            addCriterion("detail_address in", values, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressNotIn(List<String> values) {
            addCriterion("detail_address not in", values, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressBetween(String value1, String value2) {
            addCriterion("detail_address between", value1, value2, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andDetailAddressNotBetween(String value1, String value2) {
            addCriterion("detail_address not between", value1, value2, "detailAddress");
            return (Criteria) this;
        }

        public Criteria andFloorIsNull() {
            addCriterion("floor is null");
            return (Criteria) this;
        }

        public Criteria andFloorIsNotNull() {
            addCriterion("floor is not null");
            return (Criteria) this;
        }

        public Criteria andFloorEqualTo(String value) {
            addCriterion("floor =", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorNotEqualTo(String value) {
            addCriterion("floor <>", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorGreaterThan(String value) {
            addCriterion("floor >", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorGreaterThanOrEqualTo(String value) {
            addCriterion("floor >=", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorLessThan(String value) {
            addCriterion("floor <", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorLessThanOrEqualTo(String value) {
            addCriterion("floor <=", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorLike(String value) {
            addCriterion("floor like", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorNotLike(String value) {
            addCriterion("floor not like", value, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorIn(List<String> values) {
            addCriterion("floor in", values, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorNotIn(List<String> values) {
            addCriterion("floor not in", values, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorBetween(String value1, String value2) {
            addCriterion("floor between", value1, value2, "floor");
            return (Criteria) this;
        }

        public Criteria andFloorNotBetween(String value1, String value2) {
            addCriterion("floor not between", value1, value2, "floor");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginIsNull() {
            addCriterion("delivery_time_begin is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginIsNotNull() {
            addCriterion("delivery_time_begin is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_begin =", value, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginNotEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_begin <>", value, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginGreaterThan(Date value) {
            addCriterionForJDBCTime("delivery_time_begin >", value, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_begin >=", value, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginLessThan(Date value) {
            addCriterionForJDBCTime("delivery_time_begin <", value, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_begin <=", value, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_time_begin in", values, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginNotIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_time_begin not in", values, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_time_begin between", value1, value2, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBeginNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_time_begin not between", value1, value2, "deliveryTimeBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndIsNull() {
            addCriterion("delivery_time_end is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndIsNotNull() {
            addCriterion("delivery_time_end is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_end =", value, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndNotEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_end <>", value, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndGreaterThan(Date value) {
            addCriterionForJDBCTime("delivery_time_end >", value, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_end >=", value, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndLessThan(Date value) {
            addCriterionForJDBCTime("delivery_time_end <", value, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("delivery_time_end <=", value, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_time_end in", values, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndNotIn(List<Date> values) {
            addCriterionForJDBCTime("delivery_time_end not in", values, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_time_end between", value1, value2, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEndNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("delivery_time_end not between", value1, value2, "deliveryTimeEnd");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursIsNull() {
            addCriterion("working_hours is null");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursIsNotNull() {
            addCriterion("working_hours is not null");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursEqualTo(Date value) {
            addCriterionForJDBCTime("working_hours =", value, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursNotEqualTo(Date value) {
            addCriterionForJDBCTime("working_hours <>", value, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursGreaterThan(Date value) {
            addCriterionForJDBCTime("working_hours >", value, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("working_hours >=", value, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursLessThan(Date value) {
            addCriterionForJDBCTime("working_hours <", value, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("working_hours <=", value, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursIn(List<Date> values) {
            addCriterionForJDBCTime("working_hours in", values, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursNotIn(List<Date> values) {
            addCriterionForJDBCTime("working_hours not in", values, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("working_hours between", value1, value2, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWorkingHoursNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("working_hours not between", value1, value2, "workingHours");
            return (Criteria) this;
        }

        public Criteria andWeightIsNull() {
            addCriterion("weight is null");
            return (Criteria) this;
        }

        public Criteria andWeightIsNotNull() {
            addCriterion("weight is not null");
            return (Criteria) this;
        }

        public Criteria andWeightEqualTo(Integer value) {
            addCriterion("weight =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(Integer value) {
            addCriterion("weight <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(Integer value) {
            addCriterion("weight >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("weight >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(Integer value) {
            addCriterion("weight <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(Integer value) {
            addCriterion("weight <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<Integer> values) {
            addCriterion("weight in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<Integer> values) {
            addCriterion("weight not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(Integer value1, Integer value2) {
            addCriterion("weight between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(Integer value1, Integer value2) {
            addCriterion("weight not between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andOpenStatusIsNull() {
            addCriterion("open_status is null");
            return (Criteria) this;
        }

        public Criteria andOpenStatusIsNotNull() {
            addCriterion("open_status is not null");
            return (Criteria) this;
        }

        public Criteria andOpenStatusEqualTo(Byte value) {
            addCriterion("open_status =", value, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusNotEqualTo(Byte value) {
            addCriterion("open_status <>", value, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusGreaterThan(Byte value) {
            addCriterion("open_status >", value, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("open_status >=", value, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusLessThan(Byte value) {
            addCriterion("open_status <", value, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusLessThanOrEqualTo(Byte value) {
            addCriterion("open_status <=", value, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusIn(List<Byte> values) {
            addCriterion("open_status in", values, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusNotIn(List<Byte> values) {
            addCriterion("open_status not in", values, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusBetween(Byte value1, Byte value2) {
            addCriterion("open_status between", value1, value2, "openStatus");
            return (Criteria) this;
        }

        public Criteria andOpenStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("open_status not between", value1, value2, "openStatus");
            return (Criteria) this;
        }

        public Criteria andContactPersonIsNull() {
            addCriterion("contact_person is null");
            return (Criteria) this;
        }

        public Criteria andContactPersonIsNotNull() {
            addCriterion("contact_person is not null");
            return (Criteria) this;
        }

        public Criteria andContactPersonEqualTo(String value) {
            addCriterion("contact_person =", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonNotEqualTo(String value) {
            addCriterion("contact_person <>", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonGreaterThan(String value) {
            addCriterion("contact_person >", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonGreaterThanOrEqualTo(String value) {
            addCriterion("contact_person >=", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonLessThan(String value) {
            addCriterion("contact_person <", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonLessThanOrEqualTo(String value) {
            addCriterion("contact_person <=", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonLike(String value) {
            addCriterion("contact_person like", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonNotLike(String value) {
            addCriterion("contact_person not like", value, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonIn(List<String> values) {
            addCriterion("contact_person in", values, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonNotIn(List<String> values) {
            addCriterion("contact_person not in", values, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonBetween(String value1, String value2) {
            addCriterion("contact_person between", value1, value2, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andContactPersonNotBetween(String value1, String value2) {
            addCriterion("contact_person not between", value1, value2, "contactPerson");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountIsNull() {
            addCriterion("meal_address_count is null");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountIsNotNull() {
            addCriterion("meal_address_count is not null");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountEqualTo(Integer value) {
            addCriterion("meal_address_count =", value, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountNotEqualTo(Integer value) {
            addCriterion("meal_address_count <>", value, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountGreaterThan(Integer value) {
            addCriterion("meal_address_count >", value, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("meal_address_count >=", value, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountLessThan(Integer value) {
            addCriterion("meal_address_count <", value, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountLessThanOrEqualTo(Integer value) {
            addCriterion("meal_address_count <=", value, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountIn(List<Integer> values) {
            addCriterion("meal_address_count in", values, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountNotIn(List<Integer> values) {
            addCriterion("meal_address_count not in", values, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountBetween(Integer value1, Integer value2) {
            addCriterion("meal_address_count between", value1, value2, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andMealAddressCountNotBetween(Integer value1, Integer value2) {
            addCriterion("meal_address_count not between", value1, value2, "mealAddressCount");
            return (Criteria) this;
        }

        public Criteria andLonBaiduIsNull() {
            addCriterion("lon_baidu is null");
            return (Criteria) this;
        }

        public Criteria andLonBaiduIsNotNull() {
            addCriterion("lon_baidu is not null");
            return (Criteria) this;
        }

        public Criteria andLonBaiduEqualTo(BigDecimal value) {
            addCriterion("lon_baidu =", value, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduNotEqualTo(BigDecimal value) {
            addCriterion("lon_baidu <>", value, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduGreaterThan(BigDecimal value) {
            addCriterion("lon_baidu >", value, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lon_baidu >=", value, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduLessThan(BigDecimal value) {
            addCriterion("lon_baidu <", value, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lon_baidu <=", value, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduIn(List<BigDecimal> values) {
            addCriterion("lon_baidu in", values, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduNotIn(List<BigDecimal> values) {
            addCriterion("lon_baidu not in", values, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lon_baidu between", value1, value2, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLonBaiduNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lon_baidu not between", value1, value2, "lonBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduIsNull() {
            addCriterion("lat_baidu is null");
            return (Criteria) this;
        }

        public Criteria andLatBaiduIsNotNull() {
            addCriterion("lat_baidu is not null");
            return (Criteria) this;
        }

        public Criteria andLatBaiduEqualTo(BigDecimal value) {
            addCriterion("lat_baidu =", value, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduNotEqualTo(BigDecimal value) {
            addCriterion("lat_baidu <>", value, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduGreaterThan(BigDecimal value) {
            addCriterion("lat_baidu >", value, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lat_baidu >=", value, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduLessThan(BigDecimal value) {
            addCriterion("lat_baidu <", value, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lat_baidu <=", value, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduIn(List<BigDecimal> values) {
            addCriterion("lat_baidu in", values, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduNotIn(List<BigDecimal> values) {
            addCriterion("lat_baidu not in", values, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lat_baidu between", value1, value2, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLatBaiduNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lat_baidu not between", value1, value2, "latBaidu");
            return (Criteria) this;
        }

        public Criteria andLonGaodeIsNull() {
            addCriterion("lon_gaode is null");
            return (Criteria) this;
        }

        public Criteria andLonGaodeIsNotNull() {
            addCriterion("lon_gaode is not null");
            return (Criteria) this;
        }

        public Criteria andLonGaodeEqualTo(BigDecimal value) {
            addCriterion("lon_gaode =", value, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeNotEqualTo(BigDecimal value) {
            addCriterion("lon_gaode <>", value, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeGreaterThan(BigDecimal value) {
            addCriterion("lon_gaode >", value, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lon_gaode >=", value, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeLessThan(BigDecimal value) {
            addCriterion("lon_gaode <", value, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lon_gaode <=", value, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeIn(List<BigDecimal> values) {
            addCriterion("lon_gaode in", values, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeNotIn(List<BigDecimal> values) {
            addCriterion("lon_gaode not in", values, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lon_gaode between", value1, value2, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLonGaodeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lon_gaode not between", value1, value2, "lonGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeIsNull() {
            addCriterion("lat_gaode is null");
            return (Criteria) this;
        }

        public Criteria andLatGaodeIsNotNull() {
            addCriterion("lat_gaode is not null");
            return (Criteria) this;
        }

        public Criteria andLatGaodeEqualTo(BigDecimal value) {
            addCriterion("lat_gaode =", value, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeNotEqualTo(BigDecimal value) {
            addCriterion("lat_gaode <>", value, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeGreaterThan(BigDecimal value) {
            addCriterion("lat_gaode >", value, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lat_gaode >=", value, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeLessThan(BigDecimal value) {
            addCriterion("lat_gaode <", value, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lat_gaode <=", value, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeIn(List<BigDecimal> values) {
            addCriterion("lat_gaode in", values, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeNotIn(List<BigDecimal> values) {
            addCriterion("lat_gaode not in", values, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lat_gaode between", value1, value2, "latGaode");
            return (Criteria) this;
        }

        public Criteria andLatGaodeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lat_gaode not between", value1, value2, "latGaode");
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
     * company 2018-12-03
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