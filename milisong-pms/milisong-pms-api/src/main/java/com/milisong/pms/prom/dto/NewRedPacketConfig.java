package com.milisong.pms.prom.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 新人红包redis数据
 * @author sailor wang
 * @date 2018/9/21 下午4:58
 * @description
 */
@Data
public class NewRedPacketConfig {
    private Long id;
    private Long activityId;// 活动id
    private String name;// 活动名称
    private Date startDate;// 开始时间
    private Date endDate;// 结束时间
    private Byte type = 2;// 红包类型 新人红包
    private Byte isShare;// 是否同享
    private List<BigDecimal> amounts;// 红包金额组合
    private Byte valid;// 有效天数
    private String bgImage;//弹层背景图


}