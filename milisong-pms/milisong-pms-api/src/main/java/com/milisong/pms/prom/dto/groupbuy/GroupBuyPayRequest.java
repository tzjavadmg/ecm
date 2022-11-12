package com.milisong.pms.prom.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2019/5/17 3:30 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyPayRequest implements Serializable {
    private static final long serialVersionUID = -6893607193831326625L;

    //拼团记录id
    private Long userGroupBuyRecordId;

    //订单id
    private Long orderId;

}