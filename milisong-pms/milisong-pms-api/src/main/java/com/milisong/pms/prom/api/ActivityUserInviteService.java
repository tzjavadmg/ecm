package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.invite.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sailor wang
 * @date 2019/3/25 5:09 PM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface ActivityUserInviteService {

    /**
     * 创建老拉新邀请活动实例
     *
     * @param userInviteRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserInviteService/createInviteActivity")
    ResponseResult<ActivityUserInviteDto> createInviteActivity(@RequestBody UserInviteRequest userInviteRequest);

    /**
     * 查询老拉新活动实例
     *
     * @param userInviteRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserInviteService/queryInviteActivity")
    ResponseResult<UserInviteResponse> queryInviteActivity(@RequestBody UserInviteRequest userInviteRequest);

    /**
     * 查询老拉新活动实例 通过userId
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserInviteService/queryByUserId")
    ResponseResult<UserInviteResponse> queryInviteByUserId(@RequestParam("userId") Long userId);

    /**
     * 查询老拉新活动实例 通过userId
     *
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserInviteService/getConfig")
    ResponseResult<InviteConfigResponse> getConfig();

    /**
     * 查询我的邀请活动实例
     * @param userInviteRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserInviteService/queryMyInviteInfo")
    ResponseResult<UserInviteResponse> queryMyInviteInfo(@RequestBody UserInviteRequest userInviteRequest);


}