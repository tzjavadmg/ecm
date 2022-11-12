package com.milisong.pms.prom.dto.groupbuy;

import lombok.*;

/**
 * 拼团取消消息
 *
 * @author sailor wang
 * @date 2019/5/22 2:11 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBugCancelMessage {
    /**
     * 拼团实例id
     */
    private Long userGroupBuyId;
}