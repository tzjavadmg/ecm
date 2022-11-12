package com.milisong.oms.util;

import com.milisong.oms.configruation.SystemProperties;

/**
 * @ Description：系统属性工具类
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/1/16 20:44
 */
public class PropertiesUtils {

    static SystemProperties systemProperties;

    public static void setSystemProperties(SystemProperties systemProperties) {
        PropertiesUtils.systemProperties = systemProperties;
    }
}
