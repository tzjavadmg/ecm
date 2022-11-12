package com.milisong.pms.prom.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/4   10:31
 *    desc    : 用户积分流水更新用dto
 *    version : v1.0
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPointWaterDto implements Serializable {
    private static final long serialVersionUID = 7646980077687241132L;
    private Long id;

    private Long userId;

    private Byte action;

    private Byte incomeType;

    private Byte refType;

    private Long refId;

    private Integer point;

    private Date expireTime;

    private String remark;

    private Byte businessLine;

    private Date createTime;

    private Date updateTime;

}
