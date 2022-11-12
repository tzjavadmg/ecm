package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/10/26 1:48 PM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface MarketingQrcodeService {
    /**
     * 创建二维码
     *
     * @param marketingQrcode
     * @return
     */
    @PostMapping(value = "/v1/MarketingQrcodeService/createQrcode")
    ResponseResult<Boolean> createQrcode(@RequestBody MarketingQrcodeDto marketingQrcode);

    /**
     * 获取二维码列表
     *
     * @return
     */
    @PostMapping(value = "/v1/MarketingQrcodeService/qrcodeList")
    ResponseResult<List<MarketingQrcodeDto>> qrcodeList(@RequestBody MarketingQrcodeDto marketingQrcode);

    /**
     * 删除二维码
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/v1/MarketingQrcodeService/deleteQrcode")
    ResponseResult<Boolean> deleteQrcode(@RequestParam("id") Long id);

    @PostMapping(value = "/v1/MarketingQrcodeService/queryQrcode")
    ResponseResult<MarketingQrcodeDto> queryQrcode(@RequestParam("id") Long id);

    @PostMapping(value = "/v1/MarketingQrcodeService/getDetailByCode")
    ResponseResult<MarketingQrcodeDto> getDetailByCode(@RequestParam("qrcode") String qrcode);

    @PostMapping(value = "/v1/MarketingQrcodeService/updateQrcode")
    ResponseResult<Boolean> updateQrcode(@RequestBody MarketingQrcodeDto marketingQrcode);

    @PostMapping(value = "/v1/MarketingQrcodeService/getQrcode")
    ResponseResult<MarketingQrcodeDto> getQrcode(@RequestParam("id") Long id);
}