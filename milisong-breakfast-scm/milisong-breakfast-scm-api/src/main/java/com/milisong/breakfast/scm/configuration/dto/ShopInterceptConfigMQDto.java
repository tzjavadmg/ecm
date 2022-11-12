package com.milisong.breakfast.scm.configuration.dto;

import lombok.Data;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/20   16:51
 *    desc    : 门店截单配置属性发送MQDto
 *    version : v1.0
 * </pre>
 */
@Data
public class ShopInterceptConfigMQDto {
    private Long id;
    private Long shopId;
    private String shopName;
    private String shopCode;
    private String cutOffTime;
    private String startTime;
    private String endTime;
    private String firstOrdersetTime;
    private String secondOrdersetTime;
    private Integer maxCapacity;
    private String dispatchTime;
    private Boolean isDefault;
            
}
