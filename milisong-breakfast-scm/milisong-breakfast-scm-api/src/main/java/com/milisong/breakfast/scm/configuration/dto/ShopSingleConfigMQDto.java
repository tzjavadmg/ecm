package com.milisong.breakfast.scm.configuration.dto;

import lombok.Data;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/20   15:56
 *    desc    : mqdto
 *    version : v1.0
 * </pre>
 */

@Data
public class ShopSingleConfigMQDto {
    private Long shopId;
    private String shopCode;
    private String configKey;
    private String configValue;
    private Byte configValueType;
    private String configDescribe;
}