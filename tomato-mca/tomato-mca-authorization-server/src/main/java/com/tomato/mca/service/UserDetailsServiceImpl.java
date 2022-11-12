package com.tomato.mca.service;

import com.tomato.mca.sdk.MemberUser;
import com.tomato.mms.api.UserService;
import com.tomato.mms.dto.UserDto;
import com.tomatoframework.api.ResponseResult;
import com.tomatoframework.utils.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(@RequestParam("openId") String openId) throws UsernameNotFoundException {
        ResponseResult<UserDto> responseResult;
        log.info("openId={}",openId);
        //兼容上一个版本
        if (openId.contains(",")) {
            String[] paramArray = openId.split(",");
            openId = paramArray[0];
            String sourceType = paramArray[1];
            responseResult = userService.getByOpenIdAndSourceType(openId, sourceType);
        } else {
            responseResult = userService.getUserByOpenId(openId);
        }

        UserDto userInfo = responseResult.getData();
        userInfo.setPassword("TODO");
        MemberUser user = new MemberUser(userInfo.getOpenId(), userInfo.getPassword(), new HashSet<>());
        BeanMapper.copy(userInfo, user);
        user.setAgreementno(userInfo.getWxAgreementno());
        log.info("memberUser {}", user);
        return user;
    }
}
