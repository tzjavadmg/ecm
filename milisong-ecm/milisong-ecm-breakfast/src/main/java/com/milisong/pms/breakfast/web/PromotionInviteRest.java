package com.milisong.pms.breakfast.web;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.breakfast.service.RestInviteService;
import com.milisong.pms.prom.dto.invite.ActivityUserInviteDto;
import com.milisong.pms.prom.dto.invite.InviteConfigResponse;
import com.milisong.pms.prom.dto.invite.UserInviteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:32
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/breakfast/")
public class PromotionInviteRest {

    @Autowired
    RestInviteService restInviteService;

    /**
     * 创建老用户拉新用户活动
     *
     * @return
     */
    @GetMapping("create-invite-activity")
    ResponseResult<ActivityUserInviteDto> createInviteActivity() {
        return restInviteService.createInviteActivity();
    }

    /**
     * 点击已分享的老拉新邀请活动
     * @return
     */
    @GetMapping("click-sharedinvite-activity/{originateUserId}")
    ResponseResult<UserInviteResponse> clickSharedInviteActivity(@PathVariable("originateUserId")Long originateUserId) {
        return restInviteService.clickSharedInviteActivity(originateUserId);
    }

    /**
     * 查询老拉新实例
     * @return
     */
    @GetMapping("query-myinvite-info")
    ResponseResult<UserInviteResponse> queryMyInviteInfo() {
        return restInviteService.queryMyInviteInfo();
    }

    /**
     * 老拉新邀请活动入口
     * @return
     */
    @GetMapping("query-invite-entry-banner")
    ResponseResult<InviteConfigResponse> queryInviteEntryBanner() {
        return restInviteService.queryEntryBannerConfig();
    }

}