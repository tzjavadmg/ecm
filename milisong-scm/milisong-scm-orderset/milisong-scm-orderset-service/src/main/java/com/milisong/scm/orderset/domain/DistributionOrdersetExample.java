package com.milisong.scm.orderset.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DistributionOrdersetExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DistributionOrdersetExample() {
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

        public Criteria andBuildingIdIsNull() {
            addCriterion("building_id is null");
            return (Criteria) this;
        }

        public Criteria andBuildingIdIsNotNull() {
            addCriterion("building_id is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingIdEqualTo(Long value) {
            addCriterion("building_id =", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotEqualTo(Long value) {
            addCriterion("building_id <>", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdGreaterThan(Long value) {
            addCriterion("building_id >", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("building_id >=", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdLessThan(Long value) {
            addCriterion("building_id <", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdLessThanOrEqualTo(Long value) {
            addCriterion("building_id <=", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdIn(List<Long> values) {
            addCriterion("building_id in", values, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotIn(List<Long> values) {
            addCriterion("building_id not in", values, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdBetween(Long value1, Long value2) {
            addCriterion("building_id between", value1, value2, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotBetween(Long value1, Long value2) {
            addCriterion("building_id not between", value1, value2, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationIsNull() {
            addCriterion("building_abbreviation is null");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationIsNotNull() {
            addCriterion("building_abbreviation is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationEqualTo(String value) {
            addCriterion("building_abbreviation =", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotEqualTo(String value) {
            addCriterion("building_abbreviation <>", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationGreaterThan(String value) {
            addCriterion("building_abbreviation >", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationGreaterThanOrEqualTo(String value) {
            addCriterion("building_abbreviation >=", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationLessThan(String value) {
            addCriterion("building_abbreviation <", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationLessThanOrEqualTo(String value) {
            addCriterion("building_abbreviation <=", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationLike(String value) {
            addCriterion("building_abbreviation like", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotLike(String value) {
            addCriterion("building_abbreviation not like", value, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationIn(List<String> values) {
            addCriterion("building_abbreviation in", values, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotIn(List<String> values) {
            addCriterion("building_abbreviation not in", values, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationBetween(String value1, String value2) {
            addCriterion("building_abbreviation between", value1, value2, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andBuildingAbbreviationNotBetween(String value1, String value2) {
            addCriterion("building_abbreviation not between", value1, value2, "buildingAbbreviation");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIsNull() {
            addCriterion("delivery_floor is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIsNotNull() {
            addCriterion("delivery_floor is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorEqualTo(String value) {
            addCriterion("delivery_floor =", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotEqualTo(String value) {
            addCriterion("delivery_floor <>", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorGreaterThan(String value) {
            addCriterion("delivery_floor >", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorGreaterThanOrEqualTo(String value) {
            addCriterion("delivery_floor >=", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLessThan(String value) {
            addCriterion("delivery_floor <", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLessThanOrEqualTo(String value) {
            addCriterion("delivery_floor <=", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorLike(String value) {
            addCriterion("delivery_floor like", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotLike(String value) {
            addCriterion("delivery_floor not like", value, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorIn(List<String> values) {
            addCriterion("delivery_floor in", values, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotIn(List<String> values) {
            addCriterion("delivery_floor not in", values, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorBetween(String value1, String value2) {
            addCriterion("delivery_floor between", value1, value2, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andDeliveryFloorNotBetween(String value1, String value2) {
            addCriterion("delivery_floor not between", value1, value2, "deliveryFloor");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationIsNull() {
            addCriterion("company_abbreviation is null");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationIsNotNull() {
            addCriterion("company_abbreviation is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationEqualTo(String value) {
            addCriterion("company_abbreviation =", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotEqualTo(String value) {
            addCriterion("company_abbreviation <>", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationGreaterThan(String value) {
            addCriterion("company_abbreviation >", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationGreaterThanOrEqualTo(String value) {
            addCriterion("company_abbreviation >=", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationLessThan(String value) {
            addCriterion("company_abbreviation <", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationLessThanOrEqualTo(String value) {
            addCriterion("company_abbreviation <=", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationLike(String value) {
            addCriterion("company_abbreviation like", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotLike(String value) {
            addCriterion("company_abbreviation not like", value, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationIn(List<String> values) {
            addCriterion("company_abbreviation in", values, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotIn(List<String> values) {
            addCriterion("company_abbreviation not in", values, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationBetween(String value1, String value2) {
            addCriterion("company_abbreviation between", value1, value2, "companyAbbreviation");
            return (Criteria) this;
        }

        public Criteria andCompanyAbbreviationNotBetween(String value1, String value2) {
            addCriterion("company_abbreviation not between", value1, value2, "companyAbbreviation");
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

        public Criteria andDetailSetNoDescriptionIsNull() {
            addCriterion("detail_set_no_description is null");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionIsNotNull() {
            addCriterion("detail_set_no_description is not null");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionEqualTo(String value) {
            addCriterion("detail_set_no_description =", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotEqualTo(String value) {
            addCriterion("detail_set_no_description <>", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionGreaterThan(String value) {
            addCriterion("detail_set_no_description >", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("detail_set_no_description >=", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionLessThan(String value) {
            addCriterion("detail_set_no_description <", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionLessThanOrEqualTo(String value) {
            addCriterion("detail_set_no_description <=", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionLike(String value) {
            addCriterion("detail_set_no_description like", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotLike(String value) {
            addCriterion("detail_set_no_description not like", value, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionIn(List<String> values) {
            addCriterion("detail_set_no_description in", values, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotIn(List<String> values) {
            addCriterion("detail_set_no_description not in", values, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionBetween(String value1, String value2) {
            addCriterion("detail_set_no_description between", value1, value2, "detailSetNoDescription");
            return (Criteria) this;
        }

        public Criteria andDetailSetNoDescriptionNotBetween(String value1, String value2) {
            addCriterion("detail_set_no_description not between", value1, value2, "detailSetNoDescription");
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