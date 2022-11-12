package com.milisong.scm.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.constant.SysConstant;
import com.milisong.scm.orderset.api.DistributionService;
import com.milisong.scm.orderset.dto.param.DistributionSearchParam;
import com.milisong.scm.orderset.dto.param.OrderSetReqDto;
import com.milisong.scm.orderset.dto.result.DistributionOrdersetInfoResult;
import com.milisong.scm.orderset.dto.result.DistributionQueryResult;
import com.milisong.scm.orderset.dto.result.OrderSetDetailResultDto;
import com.milisong.scm.properties.OssUrlProperties;
import com.milisong.upms.utils.RestClient;
import com.milisong.upms.utils.UserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配送单rest
 *
 * @author yangzhilong
 */
@Slf4j
@RestController
@RequestMapping("/distribution")
public class DistributionRest {
    @Autowired
    private DistributionService distributionService;
    @Autowired
    private OssUrlProperties ossUrlProperties;

    @PostMapping("/page-list")
    public ResponseResult<Pagination<DistributionQueryResult>> pageSearch(@RequestBody DistributionSearchParam param) {
        return ResponseResult.buildSuccessResponse(distributionService.pageSearch(param));
    }

    @GetMapping("/orderset-info")
    public ResponseResult<List<DistributionOrdersetInfoResult>> getCustomerOrderByDistributionNo(@RequestParam("distributionNo") String distributionNo) {
        return ResponseResult.buildSuccessResponse(distributionService.getCustomerOrderByDistributionNo(distributionNo));
    }

    @PostMapping("/page-list-by-order-set")
    public ResponseResult<Pagination<OrderSetDetailResultDto>> pageSearchOrderSet(@RequestBody OrderSetReqDto dto) {
        ResponseResult<String> checkResult = checkShopPermission(dto.getShopId());
        if (!checkResult.isSuccess()) {
            return ResponseResult.buildFailResponse(checkResult.getCode(), checkResult.getMessage());
        }
        log.info("分页条件查询集单信息{}", JSON.toJSONString(dto));
        String ossResult = RestClient.post(ossUrlProperties.getSearchOrderSet(), dto);
        log.info("请求oss集单查询接口返回:{}", ossResult);
        ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
        if (result.isSuccess()) {
            return result;
        }
        return ResponseResult.buildFailResponse(result.getCode(),result.getMessage());
    }

    @GetMapping("/orderset-info-by-setno")
    public ResponseResult<List<DistributionOrdersetInfoResult>> getCustomerOrderByOrderSetNo(@RequestParam("setNo") String setNo) {
        String url = ossUrlProperties.getSearchCustomerOrder().concat(setNo);
        String ossResult = RestClient.get(url);
        log.info("请求oss集单详情查询接口返回:{}", ossResult);
        ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
        if (result.isSuccess()) {
             return result;
        }
        return ResponseResult.buildFailResponse(result.getCode(),result.getMessage());
    }

    private ResponseResult<String> checkShopPermission(Long shopId) {
        if (!UserInfoUtil.checkShopPermission(shopId)) {
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(), SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
        } else {
            return ResponseResult.buildSuccessResponse();
        }
    }
}
