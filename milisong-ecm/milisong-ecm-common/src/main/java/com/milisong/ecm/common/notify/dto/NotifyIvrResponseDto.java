package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   18:06
 *    desc    : ivr电话响应dto
 *    version : v1.0
 * </pre>
 */
@Data
public class NotifyIvrResponseDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 6956154508940948578L;

    private String uuid;

    private String mobile;

    private Boolean success;
}
