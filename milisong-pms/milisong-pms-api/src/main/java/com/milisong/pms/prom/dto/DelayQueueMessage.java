package com.milisong.pms.prom.dto;

/**
 * @author sailor wang
 * @date 2019/5/22 11:00 AM
 * @description
 */
public class DelayQueueMessage {
    /**
     * pms
     */
    private String system;
    /**
     * 业务功能模块
     */
    private String module;
    /**
     * 业务线id
     */
    private Long bizId;
    /**
     * 回调地址
     */
    private String callbackUrl;
    /**
     * request body
     */
    private String body;
    /**
     * 过期时间
     */
    private Long ttl;

    public DelayQueueMessage() {
    }

    public String getSystem() {
        return this.system;
    }

    public String getModule() {
        return this.module;
    }

    public Long getBizId() {
        return this.bizId;
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public String getBody() {
        return this.body;
    }

    public Long getTtl() {
        return this.ttl;
    }

    public void setSystem(final String system) {
        this.system = system;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public void setBizId(final Long bizId) {
        this.bizId = bizId;
    }

    public void setCallbackUrl(final String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public void setTtl(final Long ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "DelayQueueMessage{" +
                "system='" + system + '\'' +
                ", module='" + module + '\'' +
                ", bizId=" + bizId +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", body='" + body + '\'' +
                ", ttl=" + ttl +
                '}';
    }
}
