package com.milisong.ecm.breakfast.web.user;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.breakfast.dto.BfUserAddressDto;
import com.milisong.ecm.breakfast.service.RestUserService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ucs.dto.AddMiniAppTips;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.dto.UserRequest;
import com.milisong.ucs.sdk.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/8/31 下午6:00
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/user/")
public class UserRest {

    @Autowired
    private RestUserService restUserService;

    /**
     * 小程序jscode鉴权(无感知授权)
     *
     * @param userRequest
     * @return
     */
    @PostMapping("login")
    ResponseResult<UserDto> login(@RequestBody UserRequest userRequest) {
        return restUserService.login(userRequest);
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("user-info")
    ResponseResult<UserDto> getUserInfo() {
        return restUserService.getUserInfo();
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    @PostMapping("update-user")
    ResponseResult<UserDto> updateUser(@RequestBody UserRequest userRequest) {
        log.info("------- userInfo -> {}", JSON.toJSONString(UserContext.getCurrentUser()));
        return restUserService.updateUser(userRequest);
    }

    /**
     * 添加用户外卖地址
     *
     * @param address
     * @return
     */
    @Deprecated
    @PostMapping("add-deliveryaddr")
    ResponseResult<Boolean> addDeliveryAddr(@RequestBody UserDeliveryAddressDto address) {
        address.setUserId(UserContext.getCurrentUser().getId());
        return restUserService.addDeliveryAddr(address);
    }

    /**
     * 新增或修改收货地址
     *
     * @param address
     * @return
     */
    @PostMapping("add-update-addr")
    ResponseResult<Boolean> addOrUpdateAddr(@RequestBody UserDeliveryAddressDto address) {
        log.info("用户新增或修改地址{}", JSON.toJSONString(address));
        if(address.getBusinessLine() == null){
            address.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
        }
        return restUserService.addOrUpdateAddr(address);
    }

    /**
     * 根据公司楼层获取公司
     *
     * @param deliveryOfficeBuildingId
     * @param deliveryFloor
     * @return
     */
    @Deprecated
    @GetMapping("get-deliverycompany")
    ResponseResult<List<String>> getDeliveryCompany(@RequestParam(value = "companyId", required = true) Long companyId,
                                                    @RequestParam(value = "deliveryFloor", required = true) String deliveryFloor) {
        log.info("根据楼宇和楼层获取公司信息deliveryOfficeBuildingId={},deliveryFloor={}", companyId, deliveryFloor);
        return restUserService.getDeliveryCompany(companyId, deliveryFloor);

    }

    /**
     * 获取用户外卖地址
     *
     * @return
     */
    @GetMapping("get-deliveryaddr")
    ResponseResult<BfUserAddressDto> getDeliveryAddr(@RequestParam(value = "companyId", required = false) Long companyId,@RequestParam(value = "businessLine", required = false) Byte businessLine) {
        log.info("获取用户外卖地址:companyId={},businessLine={}", companyId,businessLine);
        if(businessLine == null){
            businessLine = BusinessLineEnum.BREAKFAST.getVal();
        }
        return restUserService.getDeliveryAddr(companyId,businessLine);
    }

    /**
     * 判断用户公司是否同名(针对老用户)
     *
     * @return
     */
    @GetMapping("is-point")
    ResponseResult<String> isPoint() {
        return restUserService.isPoint();
    }

    /**
     * 用户操作弹窗提示是或否
     *
     * @param companyName
     * @return
     */
    @GetMapping("operate-yesorno")
    ResponseResult<Boolean> operateYesOrNo(@RequestParam(value = "companyName", required = false) String companyName) {
        log.info("用户触发弹窗提示{}", companyName);
        return restUserService.operateYesOrNo(companyName);
    }


    /**
     * 引导添加小程序提示
     *
     * @return
     */
    @GetMapping("add-miniapp-tips")
    ResponseResult<AddMiniAppTips> addMiniAppTips() {
        return restUserService.addMiniAppTips();
    }

    /**
     * 个人中心分享banner
     *
     * @return
     */
    @GetMapping("share-friend-banner")
    ResponseResult<String> shareFriendBanner() {
        return restUserService.shareFriendBanner();
    }


}