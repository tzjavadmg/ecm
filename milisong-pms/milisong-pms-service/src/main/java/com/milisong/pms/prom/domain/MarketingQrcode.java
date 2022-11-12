package com.milisong.pms.prom.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 市场推广二维码
 */
@Getter
@Setter
public class MarketingQrcode {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 二维码名称
     */
    private String name;

    /**
     * 二维码类型: 1 个人 2 宣传单
     */
    private Byte type;

    /**
     * 二维码code
     */
    private String qrcode;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 推广人姓名
     */
    private String userName;

    /**
     * 跳转页面路径
     */
    private String pagePath;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 业务线
     */
    private Byte businessLine;

}