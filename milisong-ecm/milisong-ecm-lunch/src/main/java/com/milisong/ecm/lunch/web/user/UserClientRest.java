package com.milisong.ecm.lunch.web.user;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.lunch.service.RestUserService;

import com.milisong.ucs.dto.Pagenation;
import com.milisong.ucs.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 内部服务调用
 *
 * @author sailor wang
 * @date 2018/8/31 下午6:00
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/user/client/")
@Deprecated
public class UserClientRest {
    //FIXME

    @Autowired
    private RestUserService restUserService;


    /**
     * 修改用户信息
     *
     * @param userDto
     * @return
     */
    @PostMapping("update-user")
    ResponseResult<UserDto> updateUser(@RequestBody UserDto userDto) {
        return restUserService.updateUser(userDto);
    }

    /**
     * 批量获取用户数据
     * @param userId
     * @param fetchSize
     * @return
     */
    @GetMapping("fetch-user")
    ResponseResult<Pagenation<UserDto>> fetchUser(@RequestParam(value = "userId",required = false) Long userId, @RequestParam("fetchSize") Integer fetchSize){
        return restUserService.fetchUser(userId,fetchSize);
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("user-info")
    ResponseResult<UserDto> getUserInfo(@RequestParam(value = "userId",required = false)Long userId, @RequestParam(value = "session",required = false)String session){
        return restUserService.getUserInfo(userId,session);
    }


}