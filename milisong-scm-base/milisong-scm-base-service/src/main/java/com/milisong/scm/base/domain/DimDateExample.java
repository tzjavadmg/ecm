package com.milisong.scm.base.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DimDateExample {
    /**
     * t_dim_date
     */
    protected String orderByClause;

    /**
     * t_dim_date
     */
    protected boolean distinct;

    /**
     * t_dim_date
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public DimDateExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-25
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
     * @mbg.generated 2018-12-25
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * t_dim_date 2018-12-25
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

        public Criteria andDateIsNull() {
            addCriterion("`date` is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("`date` is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(Date value) {
            addCriterionForJDBCDate("`date` =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("`date` <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(Date value) {
            addCriterionForJDBCDate("`date` >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("`date` >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(Date value) {
            addCriterionForJDBCDate("`date` <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("`date` <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<Date> values) {
            addCriterionForJDBCDate("`date` in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("`date` not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("`date` between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("`date` not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andWeekYearIsNull() {
            addCriterion("`week_year` is null");
            return (Criteria) this;
        }

        public Criteria andWeekYearIsNotNull() {
            addCriterion("`week_year` is not null");
            return (Criteria) this;
        }

        public Criteria andWeekYearEqualTo(String value) {
            addCriterion("`week_year` =", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearNotEqualTo(String value) {
            addCriterion("`week_year` <>", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearGreaterThan(String value) {
            addCriterion("`week_year` >", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearGreaterThanOrEqualTo(String value) {
            addCriterion("`week_year` >=", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearLessThan(String value) {
            addCriterion("`week_year` <", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearLessThanOrEqualTo(String value) {
            addCriterion("`week_year` <=", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearLike(String value) {
            addCriterion("`week_year` like", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearNotLike(String value) {
            addCriterion("`week_year` not like", value, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearIn(List<String> values) {
            addCriterion("`week_year` in", values, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearNotIn(List<String> values) {
            addCriterion("`week_year` not in", values, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearBetween(String value1, String value2) {
            addCriterion("`week_year` between", value1, value2, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekYearNotBetween(String value1, String value2) {
            addCriterion("`week_year` not between", value1, value2, "weekYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearIsNull() {
            addCriterion("`week_of_year` is null");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearIsNotNull() {
            addCriterion("`week_of_year` is not null");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearEqualTo(String value) {
            addCriterion("`week_of_year` =", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearNotEqualTo(String value) {
            addCriterion("`week_of_year` <>", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearGreaterThan(String value) {
            addCriterion("`week_of_year` >", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearGreaterThanOrEqualTo(String value) {
            addCriterion("`week_of_year` >=", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearLessThan(String value) {
            addCriterion("`week_of_year` <", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearLessThanOrEqualTo(String value) {
            addCriterion("`week_of_year` <=", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearLike(String value) {
            addCriterion("`week_of_year` like", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearNotLike(String value) {
            addCriterion("`week_of_year` not like", value, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearIn(List<String> values) {
            addCriterion("`week_of_year` in", values, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearNotIn(List<String> values) {
            addCriterion("`week_of_year` not in", values, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearBetween(String value1, String value2) {
            addCriterion("`week_of_year` between", value1, value2, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andWeekOfYearNotBetween(String value1, String value2) {
            addCriterion("`week_of_year` not between", value1, value2, "weekOfYear");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekIsNull() {
            addCriterion("`day_of_week` is null");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekIsNotNull() {
            addCriterion("`day_of_week` is not null");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekEqualTo(Integer value) {
            addCriterion("`day_of_week` =", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekNotEqualTo(Integer value) {
            addCriterion("`day_of_week` <>", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekGreaterThan(Integer value) {
            addCriterion("`day_of_week` >", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekGreaterThanOrEqualTo(Integer value) {
            addCriterion("`day_of_week` >=", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekLessThan(Integer value) {
            addCriterion("`day_of_week` <", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekLessThanOrEqualTo(Integer value) {
            addCriterion("`day_of_week` <=", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekIn(List<Integer> values) {
            addCriterion("`day_of_week` in", values, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekNotIn(List<Integer> values) {
            addCriterion("`day_of_week` not in", values, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekBetween(Integer value1, Integer value2) {
            addCriterion("`day_of_week` between", value1, value2, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekNotBetween(Integer value1, Integer value2) {
            addCriterion("`day_of_week` not between", value1, value2, "dayOfWeek");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }
    }

    public static class Criteria extends BaseCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * t_dim_date 2018-12-25
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