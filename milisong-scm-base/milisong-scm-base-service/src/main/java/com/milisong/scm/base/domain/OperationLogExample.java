package com.milisong.scm.base.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperationLogExample {
    /**
     * t_operation_log
     */
    protected String orderByClause;

    /**
     * t_operation_log
     */
    protected boolean distinct;

    /**
     * t_operation_log
     */
    protected List<Criteria> oredCriteria;

    /**
     *
     * @mbg.generated 2018-12-25
     */
    public OperationLogExample() {
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
     * t_operation_log 2018-12-25
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

        public Criteria andModularIsNull() {
            addCriterion("`modular` is null");
            return (Criteria) this;
        }

        public Criteria andModularIsNotNull() {
            addCriterion("`modular` is not null");
            return (Criteria) this;
        }

        public Criteria andModularEqualTo(String value) {
            addCriterion("`modular` =", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularNotEqualTo(String value) {
            addCriterion("`modular` <>", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularGreaterThan(String value) {
            addCriterion("`modular` >", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularGreaterThanOrEqualTo(String value) {
            addCriterion("`modular` >=", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularLessThan(String value) {
            addCriterion("`modular` <", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularLessThanOrEqualTo(String value) {
            addCriterion("`modular` <=", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularLike(String value) {
            addCriterion("`modular` like", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularNotLike(String value) {
            addCriterion("`modular` not like", value, "modular");
            return (Criteria) this;
        }

        public Criteria andModularIn(List<String> values) {
            addCriterion("`modular` in", values, "modular");
            return (Criteria) this;
        }

        public Criteria andModularNotIn(List<String> values) {
            addCriterion("`modular` not in", values, "modular");
            return (Criteria) this;
        }

        public Criteria andModularBetween(String value1, String value2) {
            addCriterion("`modular` between", value1, value2, "modular");
            return (Criteria) this;
        }

        public Criteria andModularNotBetween(String value1, String value2) {
            addCriterion("`modular` not between", value1, value2, "modular");
            return (Criteria) this;
        }

        public Criteria andBussinessIdIsNull() {
            addCriterion("`bussiness_id` is null");
            return (Criteria) this;
        }

        public Criteria andBussinessIdIsNotNull() {
            addCriterion("`bussiness_id` is not null");
            return (Criteria) this;
        }

        public Criteria andBussinessIdEqualTo(String value) {
            addCriterion("`bussiness_id` =", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdNotEqualTo(String value) {
            addCriterion("`bussiness_id` <>", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdGreaterThan(String value) {
            addCriterion("`bussiness_id` >", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdGreaterThanOrEqualTo(String value) {
            addCriterion("`bussiness_id` >=", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdLessThan(String value) {
            addCriterion("`bussiness_id` <", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdLessThanOrEqualTo(String value) {
            addCriterion("`bussiness_id` <=", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdLike(String value) {
            addCriterion("`bussiness_id` like", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdNotLike(String value) {
            addCriterion("`bussiness_id` not like", value, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdIn(List<String> values) {
            addCriterion("`bussiness_id` in", values, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdNotIn(List<String> values) {
            addCriterion("`bussiness_id` not in", values, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdBetween(String value1, String value2) {
            addCriterion("`bussiness_id` between", value1, value2, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andBussinessIdNotBetween(String value1, String value2) {
            addCriterion("`bussiness_id` not between", value1, value2, "bussinessId");
            return (Criteria) this;
        }

        public Criteria andOperationTypeIsNull() {
            addCriterion("`operation_type` is null");
            return (Criteria) this;
        }

        public Criteria andOperationTypeIsNotNull() {
            addCriterion("`operation_type` is not null");
            return (Criteria) this;
        }

        public Criteria andOperationTypeEqualTo(String value) {
            addCriterion("`operation_type` =", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeNotEqualTo(String value) {
            addCriterion("`operation_type` <>", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeGreaterThan(String value) {
            addCriterion("`operation_type` >", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeGreaterThanOrEqualTo(String value) {
            addCriterion("`operation_type` >=", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeLessThan(String value) {
            addCriterion("`operation_type` <", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeLessThanOrEqualTo(String value) {
            addCriterion("`operation_type` <=", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeLike(String value) {
            addCriterion("`operation_type` like", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeNotLike(String value) {
            addCriterion("`operation_type` not like", value, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeIn(List<String> values) {
            addCriterion("`operation_type` in", values, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeNotIn(List<String> values) {
            addCriterion("`operation_type` not in", values, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeBetween(String value1, String value2) {
            addCriterion("`operation_type` between", value1, value2, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationTypeNotBetween(String value1, String value2) {
            addCriterion("`operation_type` not between", value1, value2, "operationType");
            return (Criteria) this;
        }

        public Criteria andOperationUserIsNull() {
            addCriterion("`operation_user` is null");
            return (Criteria) this;
        }

        public Criteria andOperationUserIsNotNull() {
            addCriterion("`operation_user` is not null");
            return (Criteria) this;
        }

        public Criteria andOperationUserEqualTo(String value) {
            addCriterion("`operation_user` =", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserNotEqualTo(String value) {
            addCriterion("`operation_user` <>", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserGreaterThan(String value) {
            addCriterion("`operation_user` >", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserGreaterThanOrEqualTo(String value) {
            addCriterion("`operation_user` >=", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserLessThan(String value) {
            addCriterion("`operation_user` <", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserLessThanOrEqualTo(String value) {
            addCriterion("`operation_user` <=", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserLike(String value) {
            addCriterion("`operation_user` like", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserNotLike(String value) {
            addCriterion("`operation_user` not like", value, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserIn(List<String> values) {
            addCriterion("`operation_user` in", values, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserNotIn(List<String> values) {
            addCriterion("`operation_user` not in", values, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserBetween(String value1, String value2) {
            addCriterion("`operation_user` between", value1, value2, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationUserNotBetween(String value1, String value2) {
            addCriterion("`operation_user` not between", value1, value2, "operationUser");
            return (Criteria) this;
        }

        public Criteria andOperationTimeIsNull() {
            addCriterion("`operation_time` is null");
            return (Criteria) this;
        }

        public Criteria andOperationTimeIsNotNull() {
            addCriterion("`operation_time` is not null");
            return (Criteria) this;
        }

        public Criteria andOperationTimeEqualTo(Date value) {
            addCriterion("`operation_time` =", value, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeNotEqualTo(Date value) {
            addCriterion("`operation_time` <>", value, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeGreaterThan(Date value) {
            addCriterion("`operation_time` >", value, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`operation_time` >=", value, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeLessThan(Date value) {
            addCriterion("`operation_time` <", value, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeLessThanOrEqualTo(Date value) {
            addCriterion("`operation_time` <=", value, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeIn(List<Date> values) {
            addCriterion("`operation_time` in", values, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeNotIn(List<Date> values) {
            addCriterion("`operation_time` not in", values, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeBetween(Date value1, Date value2) {
            addCriterion("`operation_time` between", value1, value2, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationTimeNotBetween(Date value1, Date value2) {
            addCriterion("`operation_time` not between", value1, value2, "operationTime");
            return (Criteria) this;
        }

        public Criteria andOperationResumeIsNull() {
            addCriterion("`operation_resume` is null");
            return (Criteria) this;
        }

        public Criteria andOperationResumeIsNotNull() {
            addCriterion("`operation_resume` is not null");
            return (Criteria) this;
        }

        public Criteria andOperationResumeEqualTo(String value) {
            addCriterion("`operation_resume` =", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeNotEqualTo(String value) {
            addCriterion("`operation_resume` <>", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeGreaterThan(String value) {
            addCriterion("`operation_resume` >", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeGreaterThanOrEqualTo(String value) {
            addCriterion("`operation_resume` >=", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeLessThan(String value) {
            addCriterion("`operation_resume` <", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeLessThanOrEqualTo(String value) {
            addCriterion("`operation_resume` <=", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeLike(String value) {
            addCriterion("`operation_resume` like", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeNotLike(String value) {
            addCriterion("`operation_resume` not like", value, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeIn(List<String> values) {
            addCriterion("`operation_resume` in", values, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeNotIn(List<String> values) {
            addCriterion("`operation_resume` not in", values, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeBetween(String value1, String value2) {
            addCriterion("`operation_resume` between", value1, value2, "operationResume");
            return (Criteria) this;
        }

        public Criteria andOperationResumeNotBetween(String value1, String value2) {
            addCriterion("`operation_resume` not between", value1, value2, "operationResume");
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
     * t_operation_log 2018-12-25
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