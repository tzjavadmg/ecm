package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   18:13
 *    desc    : 推广短信发送dto
 *    version : v1.0
 * </pre>
 */
@Data
public class AdvertiseSmsDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 2869336200077648269L;

    private Map<String,String> map;
}
