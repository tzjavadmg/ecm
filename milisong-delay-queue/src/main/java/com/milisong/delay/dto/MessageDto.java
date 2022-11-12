package com.milisong.delay.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * rabbitmq延迟队列消息数据传输对象
 */
@Getter
@Setter
public class MessageDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4899244554353462622L;

	/**
	 * 消息id(业务id+消息次数)
	 */
	private String msgId;
	
	/**
	 * 业务id
	 */
	private Long bizId;
	
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
	 * 用于回调业务系统处理业务的内容
	 * 1.mq方式时，消息内容(json字符串)
	 * 2.redis方式时，body作为key(遵守redis key规范)
	 */
	private String body;
	
	/**
	 * 过期时间
	 */
	private Long ttl;
	
}
