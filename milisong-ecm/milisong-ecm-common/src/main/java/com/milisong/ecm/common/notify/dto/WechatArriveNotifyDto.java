package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   14:07
 *    desc    : 餐送达 微信通知dto
 *    version : v1.0
 * </pre>
 */
@Data
public class WechatArriveNotifyDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 703595073859767481L;
    private Map<String, Set<String>> map;

    private String prepayId;

    private String openId;

    private String address;

}
