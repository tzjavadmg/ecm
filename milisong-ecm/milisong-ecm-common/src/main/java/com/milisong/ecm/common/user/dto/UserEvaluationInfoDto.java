package com.milisong.ecm.common.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   19:42
 *    desc    : 获取问卷展示dto
 *    version : v1.0
 * </pre>
 */
@Data
public class UserEvaluationInfoDto extends BaseDto implements Serializable {

    private Long deliveryId;
}
