package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   18:02
 *    desc    : 通知ivr下发Dto
 *    version : v1.0
 * </pre>
 */
@Data
public class NotifyIvrDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 4361413059798398231L;

    private Collection<String> mobiles;

    private String orderNo;

    private String mobile;

    private String orderNoKey;

    private String value;

}
