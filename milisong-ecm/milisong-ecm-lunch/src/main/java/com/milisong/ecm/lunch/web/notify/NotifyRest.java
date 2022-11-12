package com.milisong.ecm.lunch.web.notify;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.notify.dto.AdvertiseSmsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/9   14:54
 *    desc    : 通知业务暴露接口
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/v1/notify/")
public class NotifyRest {

    @Autowired
    private NotifyService notifyService;

    @PostMapping("sendAdvertiseSms")
    public ResponseResult<Boolean> sendAdvertiseSms(@RequestBody Map<String,String> map){
        AdvertiseSmsDto smsDto = new AdvertiseSmsDto();
        smsDto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        smsDto.setMap(map);
        notifyService.sendAdvertiseSms(smsDto);
        return ResponseResult.buildSuccessResponse(true);
    }

}
