package com.milisong.ecm.common.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   19:42
 *    desc    : 发送问卷dto
 *    version : v1.0
 * </pre>
 */
@Data
public class UserEvaluationSendDto extends BaseDto implements Serializable {

    private Long deliveryId;
}
