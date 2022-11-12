package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.invite.UserInviteRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sailor wang
 * @date 2019/3/25 5:11 PM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface UserInviteRecordService {
    /**
     * 查询老拉新获取邀请数据
     * @param originateUserId
     * @return
     */
    @PostMapping(value = "/v1/UserInviteRecordService/queryInviteRecords")
    ResponseResult<List<UserInviteRecordDto>> queryInviteRecords(@RequestParam("originateUserId") Long originateUserId);

    /**
     * 查询老拉新获取邀请数据，根据活动id来查询
     * @param activityInviteId
     * @return
     */
    @PostMapping(value = "/v1/UserInviteRecordService/queryInviteRecordsByActivityId")
    ResponseResult<List<UserInviteRecordDto>> queryInviteRecordsByActivityId(@RequestParam("activityInviteId") Long activityInviteId);


    /**
     * 新用户助力老拉新活动
     * @param userInviteRecord
     * @return
     */
    @PostMapping(value = "/v1/UserInviteRecordService/createInviteRecords")
    ResponseResult<Boolean> createInviteRecords(@RequestBody UserInviteRecordDto userInviteRecord);
}