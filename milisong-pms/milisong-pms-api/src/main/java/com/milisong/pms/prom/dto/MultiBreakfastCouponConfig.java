package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/12/11 1:39 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiBreakfastCouponConfig {

    private Long id;

    private Long activityId;
    // 券名
    private String name;
    // 参与人数
    private Byte players;
    // 展示天数
    private Integer showDays;
    // 每人每天帮别人砍几次
    private Byte clickLimit;
    // 有效天数
    private Byte valid;
    // 门槛天数
    private Integer limitDays;
    // 是否同享
    private Byte isShare;
    // 1 折扣券 2 商品券
    private Byte type;
    private List<CouponDto> coupons;
    // 分享标题
    private String shareTitle;
    // 分享图片
    private String sharePic;
    // 主图
    private String masterPic;
    // 支付完弹层图片或者页面图片
    private String shareToastPic;
    // 文案描述
    private String descript;
    // 结束时间
    private Date endDate;
    // 开始时间
    private Date startDate;
}
