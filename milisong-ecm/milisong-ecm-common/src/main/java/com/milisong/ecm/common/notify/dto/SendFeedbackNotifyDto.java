package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/22   17:42
 *    desc    : 发送用户反馈信息dto
 *    version : v1.0
 * </pre>
 */
@Data
public class SendFeedbackNotifyDto extends BaseDto {
    List<String> mobiles;
    Map<Integer, Object> map;
}
