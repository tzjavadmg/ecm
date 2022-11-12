package com.milisong.pms.prom.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2019/5/17 3:32 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyEntryResponse implements Serializable {
    private static final long serialVersionUID = 7830650726613246588L;

    //还差几个人
    private Integer leftNum;

    //是否参了该团
    private Boolean joined = false;

    //剩余多少时间，单位：秒
    private Integer userGroupBuyExpireTime;

    //拼团活动详情
    private ActivityGroupBuyDto groupBuyDetail;

}