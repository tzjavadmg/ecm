package com.tomato.mca.sdk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;

@Slf4j
public class SecurityContext {

    static private TokenStore staticTokenStore;

    static public void setStaticTokenStore(TokenStore staticTokenStore) {
        SecurityContext.staticTokenStore = staticTokenStore;
    }

    static public String getToken() {
        return OAuth2Context.getToken();
    }

    static public MemberUser getUserDeatils() {
        return OAuth2Context.getCurrentUser();
    }

    static public MemberUser getUserDeatils(Boolean reload) {
        if (reload) {
            return OAuth2Context.reloadCurrentUser();
        }
        return OAuth2Context.getCurrentUser();
    }

    static public String getMobileNo() {
        return getUserDeatils() == null ? null : getUserDeatils().getMobileNo();
    }

    static public String getWxSessionKey() {
        return getUserDeatils() == null ? null : getUserDeatils().getWxSessionKey();
    }

    static public String getOpenId() {
        return getUserDeatils() == null ? null : getUserDeatils().getOpenId();
    }

    static public Long getUserId() {
        Long userId = null;
        if (getUserDeatils() != null) {
            userId = getUserDeatils().getUserId();
            if (userId == null) {
                return getUserDeatils().getId();
            }
        }
        return userId;
    }

    static public String getIpAddr() {
        return getUserDeatils() == null ? null : getUserDeatils().getIpAddr();
    }

    static public String getUserSourceType() {
        return getUserDeatils() == null ? null : getUserDeatils().getSourceType();
    }

    /**
     * 获取用户类型（整型）
     *
     * @return
     */
    static public Integer getUserSourceTypeInteger() {
        String sourceTypeStr = getUserDeatils().getSourceType();
        if (StringUtils.isEmpty(sourceTypeStr)) {
            return null;
        }
        UserSourceTypeEnum userSourceTypeEnum = UserSourceTypeEnum.getEnumByDesc(sourceTypeStr);
        if (userSourceTypeEnum == null) {
            return null;
        }
        Integer sourceTypeInteger = UserSourceTypeEnum.getEnumByDesc(sourceTypeStr).getCode();
        return sourceTypeInteger;
    }

    static public void setIpAddr(String ipAddr) {
        OAuth2Authentication authentication = staticTokenStore.readAuthentication(getToken());
        MemberUser user = (MemberUser) getUserDeatils();
        user.setIpAddr(ipAddr);
        //更新Redis
        staticTokenStore.storeAccessToken(staticTokenStore.readAccessToken(getToken()), authentication);
    }


    static public void setMobileNo(String mobileNo) {
        OAuth2Authentication authentication = staticTokenStore.readAuthentication(getToken());
        MemberUser user = (MemberUser) authentication.getPrincipal();
        user.setMobileNo(mobileNo);
        //更新Redis
        staticTokenStore.storeAccessToken(staticTokenStore.readAccessToken(getToken()), authentication);
    }

    static public void setWxSessionKey(String sessionKey) {
        OAuth2Authentication authentication = staticTokenStore.readAuthentication(getToken());
        MemberUser user = (MemberUser) authentication.getPrincipal();
        user.setWxSessionKey(sessionKey);
        //更新Redis
        staticTokenStore.storeAccessToken(staticTokenStore.readAccessToken(getToken()), authentication);
    }


}
