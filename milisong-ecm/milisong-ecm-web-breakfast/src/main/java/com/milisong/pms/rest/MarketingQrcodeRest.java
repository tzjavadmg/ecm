package com.milisong.pms.rest;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.breakfast.service.RestMarketingQrcodeService;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 二维码管理
 *
 * @author sailor wang
 * @date 2018/11/7 4:09 PM
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/qrcode/")
public class MarketingQrcodeRest {

    @Autowired
    RestMarketingQrcodeService marketingQrcodeService;

    /**
     * 创建二维码
     *
     * @param marketingQrcode
     * @return
     */
    @PostMapping("create-update-qrcode")
    ResponseResult<Boolean> createOrUpdateQrcode(@RequestBody MarketingQrcodeDto marketingQrcode) {
        return marketingQrcodeService.createOrUpdateQrcode(marketingQrcode);
    }

    /**
     * 获取二维码列表
     *
     * @return
     */
    @GetMapping("qrcode-list")
    ResponseResult<List<MarketingQrcodeDto>> qrcodeList(@RequestParam(value = "qname",required = false) String qname,
                                                        @RequestParam(value = "cname",required = false)String cname,
                                                        @RequestParam(value = "uname",required = false)String uname ) {
        return marketingQrcodeService.qrcodeList(qname,cname,uname);
    }

    /**
     * 删除二维码
     *
     * @return
     */
    @GetMapping("delete-qrcode")
    ResponseResult<Boolean> deleteQrcode(Long id) {
        return marketingQrcodeService.deleteQrcode(id);
    }


    /**
     * 获取二维码
     *
     * @return
     */
    @GetMapping("get-qrcode")
    ResponseResult<MarketingQrcodeDto> getQrcode(Long id) {
        return marketingQrcodeService.getQrcode(id);
    }

    /**
     * 下载二维码
     *
     * @return
     */
    @GetMapping("download-qrcode")
    ResponseEntity<byte[]> downloadQrcode(Long id) {
        return marketingQrcodeService.downloadQrcode(id);
    }
}