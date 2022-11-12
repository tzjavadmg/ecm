package com.milisong.pms.prom.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/4   10:31
 *    desc    : 用户积分流水响应dto
 *    version : v1.0
 * </pre>
 */
@Data
public class UserPointWaterResDto implements Serializable {
    private static final long serialVersionUID = 7646980077688241132L;
    private Long id;

    private Long userId;

    private Byte action;

    private Byte incomeType;

    private Byte refType;

    private Long refId;

    private Integer point;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8")
    private Date expireTime;

    private String remark;

    private Byte businessLine;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
