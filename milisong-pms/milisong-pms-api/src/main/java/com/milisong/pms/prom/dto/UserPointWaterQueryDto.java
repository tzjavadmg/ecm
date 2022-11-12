package com.milisong.pms.prom.dto;

import com.farmland.core.api.PageParam;
import lombok.Data;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/4   10:36
 *    desc    : 用户积分流水信息请求dto
 *    version : v1.0
 * </pre>
 */
@Data
public class UserPointWaterQueryDto extends PageParam {

    private Byte businessLine;

    private Long id;

    private Long userId;
}
