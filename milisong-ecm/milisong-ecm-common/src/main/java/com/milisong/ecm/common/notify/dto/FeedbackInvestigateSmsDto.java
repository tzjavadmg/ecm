package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   18:12
 *    desc    : 用户反馈调查发送dto
 *    version : v1.0
 * </pre>
 */
@Data
public class FeedbackInvestigateSmsDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 3006753585003423793L;

    private Map<String,List<String>> map;
}
