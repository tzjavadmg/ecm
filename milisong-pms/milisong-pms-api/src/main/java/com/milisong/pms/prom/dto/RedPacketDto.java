package com.milisong.pms.prom.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包
 *
 * @author sailor wang
 * @date 2019/2/18 3:37 PM
 * @description
 */
@Data
public class RedPacketDto implements Serializable {

    private static final long serialVersionUID = 8871225577146956373L;

    private Long id;

    private String name;

    private Byte type;

    private Integer limitDays;

    private BigDecimal amount;

    private Byte isShare;

    private Integer validDays;

    private Byte businessLine;

    private Byte status;

    private String remark;

    private Date createTime;

    private Date updateTime;

}