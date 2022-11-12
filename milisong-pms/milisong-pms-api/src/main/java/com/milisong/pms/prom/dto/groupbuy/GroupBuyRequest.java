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
public class GroupBuyRequest implements Serializable {
    private static final long serialVersionUID = -6893607193831326625L;

    private Long userId;

    //openid
    private String openId;

    //订单id
    private Long orderId;

    //门店code
    private String shopCode;

    //拼团实例id
    private Long userGroupBuyId;

    //拼团活动id
    private Long activityGroupBuyId;

    //团长所在公司ID
    private Long companyId;

    //用户昵称
    private String nickName;

    //用户头像
    private String headPortraitUrl;

    private String formId;

}