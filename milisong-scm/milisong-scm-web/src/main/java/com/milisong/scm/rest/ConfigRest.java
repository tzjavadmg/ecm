package com.milisong.scm.rest;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.constant.SysConstant;
import com.milisong.scm.shop.api.GlobalConfigService;
import com.milisong.scm.shop.api.ShopBannerService;
import com.milisong.scm.shop.api.ShopInterceptService;
import com.milisong.scm.shop.api.ShopSingleConfigService;
import com.milisong.scm.shop.domain.ShopBanner;
import com.milisong.scm.shop.dto.config.*;
import com.milisong.upms.constant.SsoErrorConstant;
import com.milisong.upms.utils.UserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   19:44
 *    desc    : 配置rest层
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigRest {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private ShopInterceptService shopInterceptService;

    @Autowired
    private ShopBannerService shopBannerService;

    @Autowired
    private ShopSingleConfigService shopSingleConfigService;

    @PostMapping("/global/saveOrUpdate")
    public ResponseResult<GlobalConfigDto> saveOrUpdate(@RequestBody GlobalConfigDto dto){
        String updateBy = UserInfoUtil.buildUpdateBy();
        if(dto.getId() == null){
            dto.setCreateBy(updateBy);
        }else{
            dto.setUpdateBy(updateBy);
        }
        ResponseResult<GlobalConfigDto> responseResult = globalConfigService.saveOrUpdate(dto);
        return responseResult;
    }

    @GetMapping("/global/getGlobalConfig")
    public ResponseResult<List<GlobalConfigDto>> getGlobalConfig(){
        ResponseResult<List<GlobalConfigDto>> responseResult = globalConfigService.getGloableConfig();
        return responseResult;
    }

    @GetMapping("/global/getGlobalConfigById")
    public ResponseResult<GlobalConfigDto> getGlobalConfigById(@RequestParam("id") Long id){
        ResponseResult<GlobalConfigDto> responseResult = globalConfigService.getGloableConfigById(id);
        return responseResult;
    }

    @PostMapping("/global/sync")
    public ResponseResult<Boolean> sync(){
        ResponseResult<Boolean> result = globalConfigService.syncConfig();
        return result;
    }

    @PostMapping("/shopIntercept/saveOrUpdate")
    public ResponseResult<ShopInterceptConfigDto> saveOrUpdate(@RequestBody ShopInterceptConfigDto dto){
        String updateBy = UserInfoUtil.buildUpdateBy();
        if(dto.getId() == null){
            dto.setCreateBy(updateBy);
        }else{
            ResponseResult<ShopInterceptConfigDto> config = shopInterceptService.getShopInterceptorById(dto.getId());
            if(config.getData()!=null){
                dto.setShopId(config.getData().getShopId());
            }
            ResponseResult<String> checkResult = checkShopPermission(dto.getShopId());
            if(!checkResult.isSuccess()){
                return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
            }
            dto.setUpdateBy(updateBy);
        }
        ResponseResult<ShopInterceptConfigDto> responseResult = shopInterceptService.saveOrUpdate(dto);
        return responseResult;
    }

    @GetMapping("/shopIntercept/getInterceptorConfig")
    public ResponseResult<Pagination<ShopInterceptConfigDto>> getShopInterceptor(@RequestParam(value = "shopId",required = false)Long shopId,@RequestParam(value = "pageNo",required = false)Integer pageNo,@RequestParam(value = "pageSize",required = false)Integer pageSize){
        ResponseResult<String> checkResult = checkShopPermission(shopId);
        if(!checkResult.isSuccess()){
            return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
        }
        ShopInterceptConfigParam param = new ShopInterceptConfigParam(shopId);
        if(pageNo!=null){
            param.setPageNo(pageNo);
        }
        if(pageSize!=null){
            param.setPageSize(pageSize);
        }
        ResponseResult<Pagination<ShopInterceptConfigDto>> responseResult = shopInterceptService.getInterceptorConfig(param);
        return responseResult;
    }

    @GetMapping("/shopIntercept/getInterceptorConfigById")
    public ResponseResult<ShopInterceptConfigDto> getShopInterceptorById(@RequestParam("id")Long id){
        ResponseResult<ShopInterceptConfigDto> result = shopInterceptService.getShopInterceptorById(id);
        return result;
    }

    @PostMapping("/shopBanner/saveOrUpdate")
    public ResponseResult<ShopBannerDto> saveOrUpdate(@RequestBody ShopBannerDto dto){
        String updateBy = UserInfoUtil.buildUpdateBy();
        if(dto.getId() == null){
            dto.setCreateBy(updateBy);
        }else{
            ResponseResult<ShopBannerDto> config = shopBannerService.getShopBannerById(dto.getId());
            if(config.getData()!=null){
                dto.setShopId(config.getData().getShopId());
            }
            ResponseResult<String> checkResult = checkShopPermission(dto.getShopId());
            if(!checkResult.isSuccess()){
                return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
            }
            dto.setUpdateBy(updateBy);
        }
        ResponseResult<ShopBannerDto> responseResult = shopBannerService.saveOrUpdate(dto);
        return responseResult;
    }

    @GetMapping("/shopBanner/getShopBanner")
    public ResponseResult<Pagination<ShopBannerDto>> getShopBanner(@RequestParam(value = "shopId",required = false)Long shopId,@RequestParam(value = "pageNo",required = false)Integer pageNo,@RequestParam(value = "pageSize",required = false)Integer pageSize){
        ResponseResult<String> checkResult = checkShopPermission(shopId);
        if(!checkResult.isSuccess()){
            return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
        }
        ShopBannerParam param = new ShopBannerParam(shopId);
        if(pageNo!=null){
            param.setPageNo(pageNo);
        }
        if(pageSize!=null){
            param.setPageSize(pageSize);
        }
        ResponseResult<Pagination<ShopBannerDto>> responseResult = shopBannerService.getShopBanner(param);
        return responseResult;
    }

    @GetMapping("/shopBanner/getShopBannerById")
    public ResponseResult<ShopBannerDto> getShopBannerById(@RequestParam("id")Long id){
        ResponseResult<ShopBannerDto> responseResult = shopBannerService.getShopBannerById(id);
        return responseResult;
    }
    @PostMapping("/shopSingle/saveOrUpdate")
    public ResponseResult<ShopSingleConfigDto> saveOrUpdate(@RequestBody ShopSingleConfigDto dto){
        ResponseResult<String> checkResult = checkShopPermission(dto.getShopId());
        if(!checkResult.isSuccess()){
            return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
        }
        String updateBy = UserInfoUtil.buildUpdateBy();
        if(dto.getId() == null){
            dto.setCreateBy(updateBy);
        }else{
            dto.setUpdateBy(updateBy);
        }
        ResponseResult<ShopSingleConfigDto> responseResult = shopSingleConfigService.saveOrUpdate(dto);
        return responseResult;
    }

    @GetMapping("/shopSingle/getByShopSingleConfig")
    public ResponseResult<Pagination<ShopSingleConfigDto>> getShopSingleConfig(@RequestParam(value = "shopId",required = false)Long shopId,@RequestParam(value = "pageNo",required = false)Integer pageNo,@RequestParam(value = "pageSize",required = false)Integer pageSize){
        ResponseResult<String> checkResult = checkShopPermission(shopId);
        if(!checkResult.isSuccess()){
            return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
        }
        ShopSingleConfigParam param = new ShopSingleConfigParam(shopId);
        if(pageNo!=null){
            param.setPageNo(pageNo);
        }
        if(pageSize!=null){
            param.setPageSize(pageSize);
        }
        ResponseResult<Pagination<ShopSingleConfigDto>> responseResult = shopSingleConfigService.getShopSingleConfig(param);
        return responseResult;
    }

    @GetMapping("/shopSingle/getByShopSingleConfigById")
    public ResponseResult<ShopSingleConfigDto> getByShopSingleConfigById(@RequestParam("id")Long id){
        ResponseResult<ShopSingleConfigDto> responseResult = shopSingleConfigService.getByShopSingleConfigById(id);
        return responseResult;
    }

    @GetMapping("/shopSingle/queryMaxOutput")
    public ResponseResult<MaxOutputDto> queryMaxOutput(@RequestParam("shopId")Long shopId){
        ResponseResult<String> checkResult = checkShopPermission(shopId);
        if(!checkResult.isSuccess()){
            return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
        }
        return shopSingleConfigService.queryMaxOutput(shopId);
    }

    @PostMapping("/shopSingle/updateMaxOutput")
    public ResponseResult<MaxOutputDto> updateMaxOutput(@RequestBody MaxOutputDto dto){
        String updateBy = UserInfoUtil.buildUpdateBy();
        if(dto.getId() == null){
            dto.setCreateBy(updateBy);
        }else{
            ResponseResult<ShopSingleConfigDto> config = shopSingleConfigService.getByShopSingleConfigById(dto.getId());
            if(config.getData()!=null){
                dto.setShopId(config.getData().getShopId());
            }
            ResponseResult<String> checkResult = checkShopPermission(dto.getShopId());
            if(!checkResult.isSuccess()){
                return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
            }
            dto.setUpdateBy(updateBy);
        }
        return shopSingleConfigService.updateMaxOutput(dto);
    }

    @PostMapping("/shopSingle/sync")
    public ResponseResult<Boolean> syncShopSingle(@RequestParam(value = "shopId" ,required = false)Long shopId){
        ResponseResult<String> checkResult = checkShopPermission(shopId);
        if(!checkResult.isSuccess()){
            return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
        }
        ResponseResult<Boolean> result = shopSingleConfigService.syncConfig(shopId);
        return result;
    }

    private ResponseResult<String> checkShopPermission(Long shopId){
        if(!UserInfoUtil.checkShopPermission(shopId)){
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(),SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
        }else{
            return ResponseResult.buildSuccessResponse();
        }
    }
}
