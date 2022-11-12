package com.milisong.ucs.dto;

import com.farmland.core.api.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author sailor wang
 * @date 2019/1/11 3:57 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendBreakCouponRequest extends PageParam {
    // 操作人id
    private Long operatorId;

    // 操作人姓名
    private String operatorName;

    //早餐券ids
    private List<Long> couponids;

    // 发送时间
    private Date sendTime;

    // 发送所有用户
    private Boolean sendAllUser;

    // 所有公司ID
    private List<Long> deliveryOfficeBuildingIds;

    // 保存地址时间-start
    private Date saveAddrTimeStart;

    // 保存地址时间-end
    private Date saveAddrTimeEnd;

    // 手机号
    private List<String> mobiles;

    // 发送短信
    private String smsMsg;

    // 过滤的用户id
    private List<Long> excludeUserIds;

    private Byte businessLine;

}