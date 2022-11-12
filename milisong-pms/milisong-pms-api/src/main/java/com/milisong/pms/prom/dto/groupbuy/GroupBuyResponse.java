package com.milisong.pms.prom.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sailor wang
 * @date 2019/5/17 3:32 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyResponse implements Serializable {
    private static final long serialVersionUID = 7830650726613246588L;

    //还差几个人
    private Integer leftNum;

    //团长所在公司ID
    private Long companyId;

    //团长所在公司名称
    private String companyName;

    //团长所在的shopCode
    private String shopCode;

    //用户id
    private Long userCompanyId;

    //用户公司名称
    private String userCompanyName;

    //用户所在的shopCode
    private String userShopCode;

    //是否参了该团
    private Boolean joined = false;

    //拼团活动是否结束
    private Boolean activityGroupBuyisOver;

    //拼团活动剩余多少时间，单位：秒
    private Integer activityGroupBuyExpireTime;

    //用户拼团记录id
    private Long userGroupBuyRecordId;

    //拼团实例id
    private Long userGroupBuyId;

    //拼团活动id
    private Long activityGroupBuyId;

    //参加人数
    private List<JoinUser> joinUserList;

    //拼团活动详情
    private UserGroupBuyDto userGroupBuyDetail;

}