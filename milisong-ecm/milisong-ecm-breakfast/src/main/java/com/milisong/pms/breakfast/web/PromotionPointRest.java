package com.milisong.pms.breakfast.web;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.enums.RestEnum;
import com.milisong.oms.constant.OrderType;
import com.milisong.pms.prom.api.UserPointService;
import com.milisong.pms.prom.dto.Pagenation;
import com.milisong.pms.prom.dto.UserPointWaterQueryDto;
import com.milisong.pms.prom.dto.UserPointWaterResDto;
import com.milisong.ucs.sdk.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/7   17:13
 *    desc    : 促销积分系统入口
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/v1/promotion/point/")
public class PromotionPointRest {

    @Autowired
    private UserPointService userPointService;

    /**
     * 查询用户积分明细
     * @return
     */
    @GetMapping("query-user-point")
    ResponseResult<Pagenation<UserPointWaterResDto>> queryUserPointWater(@RequestParam("pageNo")Integer pageNo, @RequestParam("pageSize")Integer pageSize){
        UserPointWaterQueryDto dto = new UserPointWaterQueryDto();
        dto.setPageNo(pageNo);
        dto.setPageSize(pageSize);
        dto.setBusinessLine(OrderType.BREAKFAST.getValue());
        dto.setUserId(UserContext.getCurrentUser().getId());
        ResponseResult<Pagenation<UserPointWaterResDto>> responseResult = userPointService.queryByUserId(dto);
        if(!responseResult.isSuccess()){
            log.info("----查询用户积分失败---{}", JSON.toJSONString(responseResult));
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(),RestEnum.SYS_ERROR.getDesc());
        }
        return responseResult;
    }

    @GetMapping("rule-pic")
    ResponseResult<String> disPlayRulePic(){
        ResponseResult<String> responseResult = userPointService.getPointRulePic();
        if(!responseResult.isSuccess()){
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(),RestEnum.SYS_ERROR.getDesc());
        }
        return responseResult;
    }

}
