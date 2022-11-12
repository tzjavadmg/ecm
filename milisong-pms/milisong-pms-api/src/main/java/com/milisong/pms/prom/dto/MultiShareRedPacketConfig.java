package com.milisong.pms.prom.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 多人分享红包redis数据
 * @author sailor wang
 * @date 2018/9/21 下午4:59
 * @description
 */
@Data
public class MultiShareRedPacketConfig {
    private Long id;

    private Long activityId;// 活动id

    private String name;// 活动名称

    private Date startDate;// 开始时间

    private Date endDate;// 结束时间

    private Byte type;// 红包类型

    private Byte isShare;// 是否同享

    private BigDecimal amount;// 红包金额

    private Byte valid;// 有效天数

    private Byte players;// 需要几人砍

    private Byte clickLimit;// 每人每天帮别人砍几次

    private String shareToastPic;// 分享弹层图片

    private String masterPic;// 多人分享主图

    private String shareTitle;// 分享标题

    private String sharePic;// 分享图片

    private BigDecimal minAmount;//领取最小金额

    private BigDecimal maxAmount;//领取最大金额

    private BigDecimal minBudgetAmount;//最小预算

    private BigDecimal maxBudgetAmount;//最大预算

    private String descript;//活动规则

    private Integer showDays;// 展示天数

}