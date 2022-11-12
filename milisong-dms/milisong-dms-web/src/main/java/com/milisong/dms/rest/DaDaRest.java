package com.milisong.dms.rest;

import com.farmland.core.api.ResponseResult;
import com.milisong.dms.api.DaDaOrderService;
import com.milisong.dms.dto.shunfeng.RiderInfoDto;
import com.milisong.dms.dto.shunfeng.SfOrderReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author zhaozhonghui
 * @date 2018-10-24
 */
@Slf4j
@RestController
@RequestMapping("/dada")
public class DaDaRest {
    @Autowired
    private DaDaOrderService daDaOrderService;

    @PostMapping("/back/order-status")
    public Map<String,Object> backOrderStatus(HttpServletRequest request){
        String msg = readInputStream(request);
        log.info("达达订单配送状态回调信息:{}", msg);
        Map<String, Object> map = daDaOrderService.sendShunfengBackMsg(msg);
        return map;
    }

    /**
     * c端根据配送编号查询配送信息
     * @param deliveryNo
     * @return
     */
    @GetMapping("/query-rider-info")
    public ResponseResult<RiderInfoDto> querySfRiderInfo(@RequestParam("deliveryNo")String deliveryNo,@RequestParam("businessType")Byte businessType){
        ResponseResult<RiderInfoDto> result = daDaOrderService.getSfOrderDeliveryStatusByDeliveryNo(deliveryNo);
        return result;
    }

    /**
     * B端根据子集单ID查询配送信息
     * @param setDetailId
     * @return
     */
    @GetMapping("/get-rider-info")
    public ResponseResult<RiderInfoDto> getSfRiderInfo(@RequestParam("setDetailId")Long setDetailId){
        ResponseResult<RiderInfoDto> result = daDaOrderService.getSfOrderDeliveryStatusBySetDetailId(setDetailId);
        return result;
    }

    @GetMapping("/create")
    public ResponseResult createOrder(){
        SfOrderReqDto dto = new SfOrderReqDto();
        ResponseResult result = daDaOrderService.testCrestOrder(dto);
        return result;
    }

    /**
     * 读取request里的Inputstream
     *
     * @param request
     * @return
     */
    private String readInputStream(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                msg.append(line);
            }
        } catch (IOException e) {
            log.error("读取request的inputStream发生错误", e);
        }
        return msg.toString();
    }


}
