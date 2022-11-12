package com.milisong.breakfast.scm.rest;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.configuration.api.GlobalConfigService;
import com.milisong.breakfast.scm.configuration.api.ShopBannerService;
import com.milisong.breakfast.scm.configuration.api.ShopInterceptService;
import com.milisong.breakfast.scm.configuration.api.ShopSingleConfigService;
import com.milisong.breakfast.scm.configuration.dto.*;
import com.milisong.breakfast.scm.constant.SysConstant;
import com.milisong.upms.utils.UserInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   19:44
 *    desc    : 配置rest层
 *    version : v1.0
 * </pre>
 */
@Api(tags = "配置管理")
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

    @ApiOperation("更新或新增全局配置")
    @PostMapping("/global/save-or-update")
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

    @ApiOperation("查询所有全局配置")
    @GetMapping("/global/get-global-config")
    public ResponseResult<List<GlobalConfigDto>> getGlobalConfig(){
        ResponseResult<List<GlobalConfigDto>> responseResult = globalConfigService.getGloableConfig();
        return responseResult;
    }

    @ApiOperation("查询全局配置通过ID")
    @GetMapping("/global/get-global-config-by-id")
    public ResponseResult<GlobalConfigDto> getGlobalConfigById(@RequestParam("id") Long id){
        ResponseResult<GlobalConfigDto> responseResult = globalConfigService.getGloableConfigById(id);
        return responseResult;
    }

    @ApiOperation("同步全局配置")
    @PostMapping("/global/sync")
    public ResponseResult<Boolean> sync(){
        ResponseResult<Boolean> result = globalConfigService.syncConfig();
        return result;
    }

    @ApiOperation("新增或更新截单配置")
    @PostMapping("/shop-intercept/save-or-update")
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

    @ApiOperation("查询截单配置")
    @GetMapping("/shop-intercept/get-interceptor-config")
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

    @ApiOperation("查询截单配置通过ID")
    @GetMapping("/shop-intercept/get-interceptor-config-by-id")
    public ResponseResult<ShopInterceptConfigDto> getShopInterceptorById(@RequestParam("id")Long id){
        ResponseResult<ShopInterceptConfigDto> result = shopInterceptService.getShopInterceptorById(id);
        return result;
    }

    @ApiOperation("新增或更新banner图")
    @PostMapping("/shop-banner/save-or-update")
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

    @ApiOperation("查询门店Banner图")
    @GetMapping("/shop-banner/get-shop-banner")
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

    @ApiOperation("查询门店Banner图通过ID")
    @GetMapping("/shop-banner/get-shop-banner-by-id")
    public ResponseResult<ShopBannerDto> getShopBannerById(@RequestParam("id")Long id){
        ResponseResult<ShopBannerDto> responseResult = shopBannerService.getShopBannerById(id);
        return responseResult;
    }

    @ApiOperation("新增或更新门店属性")
    @PostMapping("/shop-single/save-or-update")
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

    @ApiOperation("查询门店属性配置")
    @GetMapping("/shop-single/get-by-shop-single-config")
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

    @ApiOperation("查询门店属性配置通过id")
    @GetMapping("/shop-single/get-by-shop-single-config-by-id")
    public ResponseResult<ShopSingleConfigDto> getByShopSingleConfigById(@RequestParam("id")Long id){
        ResponseResult<ShopSingleConfigDto> responseResult = shopSingleConfigService.getByShopSingleConfigById(id);
        return responseResult;
    }

    @ApiOperation("查询门店最大生产量")
    @GetMapping("/shop-single/query-max-output")
    public ResponseResult<MaxOutputDto> queryMaxOutput(@RequestParam("shopId")Long shopId){
        ResponseResult<String> checkResult = checkShopPermission(shopId);
        if(!checkResult.isSuccess()){
            return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
        }
        return shopSingleConfigService.queryMaxOutput(shopId);
    }

    @ApiOperation("更新门店最大生产量")
    @PostMapping("/shop-single/update-max-output")
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

    @ApiOperation("同步门店属性配置")
    @PostMapping("/shop-single/sync")
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
