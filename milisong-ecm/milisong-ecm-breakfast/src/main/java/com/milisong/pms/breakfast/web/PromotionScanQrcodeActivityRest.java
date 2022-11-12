package com.milisong.pms.breakfast.web;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.breakfast.service.RestScanQrcodeService;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 扫码领券接口
 *
 * @author sailor wang
 * @date 2018/9/14 下午7:32
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/breakfast/")
public class PromotionScanQrcodeActivityRest {

    @Autowired
    RestScanQrcodeService restScanQrcodeService;

    /**
     * 根据二维码获取扫码活动详情
     *
     * @param qrcode
     * @return
     */
    @GetMapping("get-scanqrcode-detail")
    ResponseResult<ScanQrcodeResponse> queryScanQrcodeActivityByQrcode(@RequestParam("qrcode") String qrcode) {
        return restScanQrcodeService.queryScanQrcodeActivityByQrcode(qrcode);
    }

    /**
     * 领取扫码活动券
     *
     * @param qrcode
     * @return
     */
    @GetMapping("receive-coupon-by-scanqrcode")
    ResponseResult<ScanQrcodeResponse> receiveCouponByQrcode(@RequestParam("qrcode") String qrcode) {
        return restScanQrcodeService.receiveCouponByQrcode(qrcode);
    }


}