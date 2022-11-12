package com.milisong.ucs.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ucs.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/8/31 下午5:38
 * @description
 */
@FeignClient("milisong-ucs-service")
public interface UserService {

	@PostMapping(value = "/v1/UserService/login")
    ResponseResult<UserDto> login(@RequestBody UserRequest userRequest);

	@PostMapping(value = "/v1/UserService/updateByOpenid")
    ResponseResult<Boolean> updateByOpenid(@RequestBody UserDto userDto);

	@PostMapping(value = "/v1/UserService/getUserInfo")
    ResponseResult<UserDto> getUserInfo(@RequestBody UserDto userDto);

	@PostMapping(value = "/v1/UserService/updateUser")
    ResponseResult<UserDto> updateUser(@RequestBody UserDto userDto);

	@PostMapping(value = "/v1/UserService/fetchUser")
    ResponseResult<Pagenation<UserDto>> fetchUser(@RequestBody UserDto userDto);

    @PostMapping(value = "/v1/UserService/queryUser")
    ResponseResult<Pagenation<UserDto>> queryUser(@RequestBody SendLunchRedPacketRequest redPacketRequest);

	@PostMapping(value = "/v1/UserService/addMiniAppTips")
    ResponseResult<AddMiniAppTips> addMiniAppTips(@RequestBody UserDto userDto);

    @PostMapping(value = "/v1/UserService/fetchUserInfoByIds")
    ResponseResult<List<UserDto>> fetchUserInfoByIds(@RequestBody UserDto userDto);

    @PostMapping(value = "/v1/UserService/fetchUserInfoByMobiles")
    ResponseResult<List<UserDto>> fetchUserInfoByMobiles(@RequestBody UserDto userDto);

    @PostMapping(value = "/v1/UserService/calcPoint")
    ResponseResult<Boolean> calcPoint(@RequestBody UserDto userDto);
}