package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/9/13 下午4:55
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRedPacketDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -658378387244001218L;
    private Long id;

    private Long activityId;

    private String name;

    private String descript;//活动说明

    private Date startDate;// 开始时间

    private Date endDate;// 结束时间

    private Byte type;// 红包类型

    private Integer userLimit;// 可发起砍红包次数

    private Integer activityLimit;// 活动限制次数

    private String dayOfWeek;

    private Byte isAllGoods;

    private String goodsCatalogIds;

    private String goodsIds;

    private String cityIds;

    private String buildingIds;

    private String companyIds;

    private BigDecimal minAmount;//最少领取多少

    private BigDecimal maxAmount;//最多领取多少

    private BigDecimal minBudgetAmount;// 预算最小金额

    private BigDecimal maxBudgetAmount;// 预算最大金额

    private Byte status;// 0 保存、1 保存并发布、2 上线、3 下线

    private Byte isShare;// 是否同享

    private BigDecimal amount;

    private List<BigDecimal> amounts;

    private Byte players;// 需要几人砍

    private Byte valid;// 有效天数

    private Byte clickLimit;// 每人每天帮别人砍几次

    private String shareTitle;//分享title

    private String sharePic;//分享图片

    private Integer showDays;// 展示天数

    private String bgImage;//弹层背景图

    private String shareToastPic;// 分享弹层图片

    private String masterPic;// 多人分享主图


}