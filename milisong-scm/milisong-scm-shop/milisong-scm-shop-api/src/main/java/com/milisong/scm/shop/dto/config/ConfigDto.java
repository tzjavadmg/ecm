package com.milisong.scm.shop.dto.config;

import lombok.Data;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   19:01
 *    desc    : 配置dto
 *    version : v1.0
 * </pre>
 */
@Data
public class ConfigDto <T> {

    private T data;

    //配置信息配型：global_input，banner_input，intercept_input，single_input
    private String type;
    //业务线：1-ecm，2-scm，3-pos，4-pms, 0-common
    private String[] serviceLine;

    private String operation;

}
