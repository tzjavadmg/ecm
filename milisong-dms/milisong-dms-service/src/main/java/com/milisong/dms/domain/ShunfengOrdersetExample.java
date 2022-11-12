/* https://github.com/orange1438 */
package com.milisong.dms.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShunfengOrdersetExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShunfengOrdersetExample() {
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

        public Criteria andDetailSetIdIsNull() {
            addCriterion("detail_set_id is null");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdIsNotNull() {
            addCriterion("detail_set_id is not null");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdEqualTo(Long value) {
            addCriterion("detail_set_id =", value, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdNotEqualTo(Long value) {
            addCriterion("detail_set_id <>", value, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdGreaterThan(Long value) {
            addCriterion("detail_set_id >", value, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdGreaterThanOrEqualTo(Long value) {
            addCriterion("detail_set_id >=", value, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdLessThan(Long value) {
            addCriterion("detail_set_id <", value, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdLessThanOrEqualTo(Long value) {
            addCriterion("detail_set_id <=", value, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdIn(List<Long> values) {
            addCriterion("detail_set_id in", values, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdNotIn(List<Long> values) {
            addCriterion("detail_set_id not in", values, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdBetween(Long value1, Long value2) {
            addCriterion("detail_set_id between", value1, value2, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andDetailSetIdNotBetween(Long value1, Long value2) {
            addCriterion("detail_set_id not between", value1, value2, "detailSetId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdIsNull() {
            addCriterion("shunfeng_order_id is null");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdIsNotNull() {
            addCriterion("shunfeng_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdEqualTo(String value) {
            addCriterion("shunfeng_order_id =", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdNotEqualTo(String value) {
            addCriterion("shunfeng_order_id <>", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdGreaterThan(String value) {
            addCriterion("shunfeng_order_id >", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("shunfeng_order_id >=", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdLessThan(String value) {
            addCriterion("shunfeng_order_id <", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdLessThanOrEqualTo(String value) {
            addCriterion("shunfeng_order_id <=", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdLike(String value) {
            addCriterion("shunfeng_order_id like", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdNotLike(String value) {
            addCriterion("shunfeng_order_id not like", value, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdIn(List<String> values) {
            addCriterion("shunfeng_order_id in", values, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdNotIn(List<String> values) {
            addCriterion("shunfeng_order_id not in", values, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdBetween(String value1, String value2) {
            addCriterion("shunfeng_order_id between", value1, value2, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andShunfengOrderIdNotBetween(String value1, String value2) {
            addCriterion("shunfeng_order_id not between", value1, value2, "shunfengOrderId");
            return (Criteria) this;
        }

        public Criteria andCredateTimeIsNull() {
            addCriterion("credate_time is null");
            return (Criteria) this;
        }

        public Criteria andCredateTimeIsNotNull() {
            addCriterion("credate_time is not null");
            return (Criteria) this;
        }

        public Criteria andCredateTimeEqualTo(Date value) {
            addCriterion("credate_time =", value, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeNotEqualTo(Date value) {
            addCriterion("credate_time <>", value, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeGreaterThan(Date value) {
            addCriterion("credate_time >", value, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("credate_time >=", value, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeLessThan(Date value) {
            addCriterion("credate_time <", value, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeLessThanOrEqualTo(Date value) {
            addCriterion("credate_time <=", value, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeIn(List<Date> values) {
            addCriterion("credate_time in", values, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeNotIn(List<Date> values) {
            addCriterion("credate_time not in", values, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeBetween(Date value1, Date value2) {
            addCriterion("credate_time between", value1, value2, "credateTime");
            return (Criteria) this;
        }

        public Criteria andCredateTimeNotBetween(Date value1, Date value2) {
            addCriterion("credate_time not between", value1, value2, "credateTime");
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

    /**
     * 顺丰订单对应子集单关系表shunfeng_orderset的映射实体
     */
    public static class Criteria extends BaseCriteria {

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