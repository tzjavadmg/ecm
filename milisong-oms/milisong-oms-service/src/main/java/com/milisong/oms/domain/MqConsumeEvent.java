package com.milisong.oms.domain;

import lombok.Getter;
import lombok.Setter;

/**

 */
@Getter
@Setter
public class MqConsumeEvent {
    /**
     * 
     */
    private Long id;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息主题
     */
    private String messageTopic;

    /**
     * 
     */
    private String consumer;

    /**
     * 消息状态：0:已接收;1:已反馈;
     */
    private Byte status;

    /**
     * 消息体
     */
    private String mesageBody;
}