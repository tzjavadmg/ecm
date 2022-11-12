package com.milisong.ecm.lunch.web.user;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.breakfast.service.RestUserService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.lunch.service.LunchService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/4/12   16:44
 *    desc    :
 *    version : v1.0
 * </pre>
 */

@Slf4j
@RestController
@RequestMapping("/lc")
public class UserRestLC {

    @Autowired
    private LunchService lunchService;

    @Autowired
    private RestUserService restUserService;

    /**
     * 判断用户公司是否同名(针对老用户)
     * @return
     */
    @GetMapping("/v1/user/is-point")
    public ResponseResult<?> isPoint(){
        return lunchService.isPoint();
    }

    /**
     * 获取用户外卖地址
     * @return
     */
    @GetMapping("/v1/user/get-deliveryaddr")
    ResponseResult<?> getDeliveryAddr(@RequestParam(value = "deliveryOfficeBuildingId" , required = false) Long deliveryOfficeBuildingId){
        log.info("午餐-获取用户外卖地址:companyId={}", deliveryOfficeBuildingId);
        return restUserService.getDeliveryAddr(deliveryOfficeBuildingId,BusinessLineEnum.LUNCH.getVal());
    }

    /**
     * 新增或修改收货地址
     * @param address
     * @return
     */
    @PostMapping("/v1/user/add-update-addr")
    ResponseResult<?> addOrUpdateAddr(@RequestBody UserDeliveryAddressDto address) {
        log.info("午餐-用户新增或修改地址{}",JSON.toJSONString(address));
        address.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        return restUserService.addOrUpdateAddr(address);
    }
}
