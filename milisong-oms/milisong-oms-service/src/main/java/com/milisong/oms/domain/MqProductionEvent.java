package com.milisong.oms.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**

 */
@Getter
@Setter
public class MqProductionEvent {
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
    private String producer;

    /**
     * 消息状态：0:已发送;1:发送成功;2:发送失败;
     */
    private Byte status;

    /**
     * 到达时间
     */
    private Date arrivedTime;

    /**
     * 消息体
     */
    private String messageBody;
}