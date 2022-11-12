package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sailor wang
 * @date 2018/9/13 上午11:54
 * @description
 */
@FeignClient("milisong-pms-service")
public interface MultiShareActivityService {

    @PostMapping(value = "/v1/MultiShareActivityService/multiShareLaunchQuery")
    ResponseResult<RedPacketLaunchResultDto> multiShareLaunchQuery(@RequestBody RedPacketLaunchQueryDto redPacketLaunchQuery);
}