package com.milisong.upms.utils;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.farmland.core.api.ResponseResult;
import com.milisong.upms.constant.SsoConstant;
import com.milisong.upms.dto.ShopDto;
import com.milisong.upms.dto.UserDataPermissionDto;
import com.milisong.upms.dto.UserInfoDto;
import com.milisong.upms.service.HttpService;

/**
 * 获取用户信息的帮助类
 *
 * @author yangzhilong
 */
public class UserInfoUtil {
	private static HttpService httpService;
	public static void setHttpService(HttpService service) {
		httpService = service;
	}

    /**
     * 获取当前用户信息
     * @return
     */
    public static UserInfoDto getCurrentUserInfo() {
    	ResponseResult<UserInfoDto> result = httpService.getUserInfo(getToken());
    	if(result.isSuccess()) {
    		return result.getData();
    	}
    	return null;
    }
    
    /**
     * 获取当前用户门店权限
     * @return
     */
    public static List<UserDataPermissionDto> getCurrentUserShopPermissionList() {
    	ResponseResult<List<UserDataPermissionDto>> result = httpService.getShopPermissionList(getToken());
    	if(result.isSuccess()) {
    		return result.getData();
    	}
    	return null;
    }
    
    /**
     * 获取当前用户门店id集合
     * @return
     */
    public static List<String> getCurrentUserShopIdList() {
    	ResponseResult<List<ShopDto>> result = httpService.getCurrentUserShopList(getToken());
    	if(result.isSuccess() && org.apache.commons.collections.CollectionUtils.isNotEmpty(result.getData())) {
    		return result.getData().stream().map(item -> String.valueOf(item.getShopId())).collect(Collectors.toList());
    	}
    	return null;
    }
    
    /**
     * 获取当前用户门店code集合
     * @return
     */
    public static List<String> getCurrentUserShopCodeList() {
    	ResponseResult<List<ShopDto>> result = httpService.getCurrentUserShopList(getToken());
    	if(result.isSuccess() && org.apache.commons.collections.CollectionUtils.isNotEmpty(result.getData())) {
    		return result.getData().stream().map(item -> item.getShopCode()).collect(Collectors.toList());
    	}
    	return null;
    }
    
    /**
     * 拼装当前用户信息
     * @return
     */
    public static String buildUpdateBy(){
        UserInfoDto userInfoDto = getCurrentUserInfo();
        if (null != userInfoDto) {
            return userInfoDto.getUserNo() + "_" + userInfoDto.getUserName();
        }
        return null;

    }

    public static Boolean checkShopPermission(Long shopId){
        List<String> idList = getCurrentUserShopIdList();
        if(idList == null || idList.size() == 0){
            return false;
        }
        String id = Long.toString(shopId);
        if(!idList.stream().anyMatch(o-> o.equals(id))) {
            return false;
        }else{
            return true;
        }
    }

    public static Boolean checkShopPermission(String shopCode){
        List<String> idList = getCurrentUserShopCodeList();
        if(idList == null || idList.size() == 0){
            return false;
        }
        if(!idList.stream().anyMatch(o-> o.equals(shopCode))) {
            return false;
        }else{
            return true;
        }
    }

   /**
    * 获取token
    * @return
    */
   private static String getToken() {
	   HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
       String token = request.getHeader(SsoConstant.TOKEN);
       if(StringUtils.isBlank(token)) {
           token = request.getParameter(SsoConstant.TOKEN);
       }
       return token;
   }
}
