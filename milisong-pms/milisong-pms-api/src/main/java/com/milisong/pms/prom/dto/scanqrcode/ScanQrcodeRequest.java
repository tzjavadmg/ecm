package com.milisong.pms.prom.dto.scanqrcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/5/10 1:48 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanQrcodeRequest {

    private Long userId;

    /**
     * 新用户标识
     */
    private Byte isNew;

    /**
     * 二维码code
     */
    private String qrcode;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 礼物类型
     */
    private Byte giftType;

    /**
     * 礼物ids
     */
    private String giftIds;

    /**
     * 背景图片
     */
    private String bgImg;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    private Byte businessLine;

}