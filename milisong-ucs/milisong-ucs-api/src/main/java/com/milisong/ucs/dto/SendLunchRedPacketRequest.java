package com.milisong.ucs.dto;

import com.farmland.core.api.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 批量发送午餐红包
 *
 * @author sailor wang
 * @date 2019/2/18 3:56 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendLunchRedPacketRequest extends PageParam {
    private Long id;

    // 操作人id
    private Long operatorId;

    // 操作人姓名
    private String operatorName;

    //午餐红包ids
    private List<Long> redpacketids;

    // 红包名称
    private String redpacketName;

    // 发送时间
    private Date sendTime;

    // 发送所有用户
    private Boolean sendAllUser;

    // 手机号
    private List<String> mobiles;

    // 手机号文本
    private String mobileText;

    // 发送短信
    private String smsMsg;

    // 过滤的用户id
    private List<Long> excludeUserIds;

    private Byte businessLine;

}