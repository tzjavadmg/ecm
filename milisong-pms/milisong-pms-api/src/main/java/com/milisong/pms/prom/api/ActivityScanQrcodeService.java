package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeRequest;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户扫码送礼品活动
 *
 * @author sailor wang
 * @date 2019/5/9 9:01 PM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface ActivityScanQrcodeService {
    /**
     * 创建扫码送礼物活动
     *
     * @param scanQrcodeRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityScanQrcodeService/createScanQrcodeActivity")
    ResponseResult<Boolean> createScanQrcodeActivity(@RequestBody ScanQrcodeRequest scanQrcodeRequest);

    /**
     * 二维码绑定扫码活动
     *
     * @param scanQrcodeRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityScanQrcodeService/bindQrcodeAndScanQrcodeActivity")
    ResponseResult<Boolean> bindQrcodeAndScanQrcodeActivity(@RequestBody ScanQrcodeRequest scanQrcodeRequest);

    /**
     * 根据二维码code获取绑定的活动
     *
     * @param scanQrcodeRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityScanQrcodeService/queryScanQrcodeActivityByQrcode")
    ResponseResult<ScanQrcodeResponse> queryScanQrcodeActivityByQrcode(@RequestBody ScanQrcodeRequest scanQrcodeRequest);

    /**
     * 根据二维码code领取券
     *
     * @param scanQrcodeRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityScanQrcodeService/receiveCouponByQrcode")
    ResponseResult<ScanQrcodeResponse> receiveCouponByQrcode(@RequestBody ScanQrcodeRequest scanQrcodeRequest);

}