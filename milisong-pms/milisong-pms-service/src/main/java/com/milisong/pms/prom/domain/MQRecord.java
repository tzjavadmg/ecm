package com.milisong.pms.prom.domain;

import java.util.Date;
import lombok.Data;

/**
 * @description:mq_record表的实体类
 * @version:  1.0
 * @author:  田海波
 * @createTime: 2019-01-10 16:15:28
 */
@Data
public class MQRecord {
    private Long id;

    /**
     * 业务标识 1-积分，2-红包，3-券
     */
    private Byte bizType;

    /**
     * 虚拟主机
     */
    private String virtualHost;

    /**
     * 通道
     */
    private String channel;

    /**
     * 组
     */
    private String mqGroup;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 业务方法名（包括类名）
     */
    private String bizMethodName;

    /**
     * 是否处理成功, 1-成功，0-失败
     */
    private Boolean success;

    /**
     * 消息头id
     */
    private String messageHeadersId;

    private Date createTime;

    private Date updateTime;

    /**
     * 消息内容
     */
    private String messageData;
}