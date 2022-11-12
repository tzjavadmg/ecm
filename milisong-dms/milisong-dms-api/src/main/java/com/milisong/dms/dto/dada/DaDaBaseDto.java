package com.milisong.dms.dto.dada;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @date 2018-11-26
 */
@Data
public class DaDaBaseDto implements Serializable {
    private static final long serialVersionUID = -5463410179024079203L;
    /** 开发者账号app_key */
    private String appKey;
    /** 签名 */
    private String signature;
    /** 时间戳（秒） */
    private String timestamp;
    /** 请求格式 json */
    private String format;
    /** api 版本 1.0 */
    private String v;
    /** 商户编号（创建商户账号分配的编号） */
    private String sourceId;
    /** 业务参数 */
    private String body;
}
