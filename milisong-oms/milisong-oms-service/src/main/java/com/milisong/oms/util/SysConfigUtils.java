package com.milisong.oms.util;

import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.service.config.BusinessLineConfigService;

/**
 * @ Description：业务线订单配置工具类
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/4 14:24
 */
public class SysConfigUtils {

    static BusinessLineConfigService businessLineConfigService;

    public static void setBusinessLineConfigService(BusinessLineConfigService businessLineConfigService) {
        SysConfigUtils.businessLineConfigService = businessLineConfigService;
    }

    static SystemProperties systemProperties;

    public static void setSystemProperties(SystemProperties systemProperties) {
        PropertiesUtils.systemProperties = systemProperties;
    }

    public static Integer getUnPayExpiredTime() {
        return businessLineConfigService.getConfig().getUnPayExpiredTime();
    }

    public static Integer getUnPayExpiredTime(Byte businessLine) {
        return businessLineConfigService.getConfig(businessLine).getUnPayExpiredTime();
    }

    public static Integer getPointRatio() {
        return PropertiesUtils.systemProperties.getPointRatio();
    }

    public static Long getGroupBuyPayExpiredTime() {
        return PropertiesUtils.systemProperties.getWechatPay().getGroupBuyExpiredTime();
    }

}
