package com.milisong.pos.production.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelayMessageDto {
    /**
     * 系统名称
     */
    private String system;
    /**
     * 模块名称
     */
    private String module;
    /**
     * 业务id
     */
    private Long bizId;
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
