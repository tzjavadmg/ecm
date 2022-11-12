package com.milisong.upms.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.ResponseResult;
import com.milisong.upms.constant.SsoConstant;
import com.milisong.upms.dto.MenuDto;
import com.milisong.upms.dto.ShopDto;
import com.milisong.upms.dto.UserInfoDto;
import com.milisong.upms.service.HttpService;

@RestController
@RequestMapping("/upms")
public class SsoRest {
	@Autowired
	private HttpService httpService;
	
    /**
     * 获取token
     *
     * @param ticket
     * @return
     */
    @GetMapping("/token")
    public ResponseResult<String> getMLSToken(@RequestParam("ticket") String ticket) {
        return httpService.getMilisongToken(ticket);
    }

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/user")
    public ResponseResult<UserInfoDto> getUserInfo(@RequestHeader(SsoConstant.TOKEN) String token) {
        return httpService.getUserInfo(token);
    }

    /**
     * 获取用户菜单
     *
     * @param token
     * @return
     */
    @GetMapping("/menu")
    public ResponseResult<List<MenuDto>> getUserMenu(@RequestHeader(SsoConstant.TOKEN) String token) {
    	return httpService.getUserMenu(token);
    }

    /**
     * 获取有权限的门店的id-名字的list
     * @param token
     * @return
     */
    @GetMapping("/data-permission-shop")
    public ResponseResult<List<ShopDto>> getCurrentUserShopIdList(@RequestHeader(SsoConstant.TOKEN) String token) {
        return httpService.getCurrentUserShopList(token);
    }
}
