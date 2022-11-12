package com.milisong.delay.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息生产表
 */
@Getter
@Setter
public class DelayProductionEvent {
    /**
     * 
     */
    private Long id;

    /**
     * 业务id
     */
    private Long bizId;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息状态：1:发送成功,2:发送失败;
     */
    private Byte status;

    /**
     * 系统名称
     */
    private String system;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 回调地址
     */
    private String callbackUrl;

    /**
     * 过期时间
     */
    private Long ttl;

    /**
     * 类型(1:MQ消息 2:redis消息)
     */
    private Integer type;

    /**
     * 消息体
     */
    private String messageBody;
}