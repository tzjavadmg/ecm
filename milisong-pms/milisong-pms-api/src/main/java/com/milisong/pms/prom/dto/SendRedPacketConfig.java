package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 给用户发送红包，用于促活
 *
 * @author sailor wang
 * @date 2018/11/6 6:33 PM
 * @description
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendRedPacketConfig {

    private Long acitivtyId;

    // 红包金额发送条件
    private BigDecimal conditionAmount;

    //红包名称
    private String name;

    private List<BigDecimal> amount;

    // 红包类型
    private Byte type;

    // 是否同享
    private Byte isShare;

    // 短信内容
    private String smsMsg;

    // 有效天数
    private Byte valid;

    // 开始时间
    private Date startDate;

    // 结束时间
    private Date endDate;

}