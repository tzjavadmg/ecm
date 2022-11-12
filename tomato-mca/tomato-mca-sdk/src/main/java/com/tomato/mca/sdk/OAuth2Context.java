package com.tomato.mca.sdk;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.http.HttpServletRequest;

public class OAuth2Context {

    private final static ThreadLocal<String> token = new ThreadLocal<String>();

    private final static ThreadLocal<MemberUser> currentUser = new ThreadLocal<MemberUser>();

    static private TokenStore tokenStore;

    static public void setTokenStore(TokenStore tokenStore){
        OAuth2Context.tokenStore=tokenStore;
    }

    static public String getToken() {
        return token.get();
    }

    static public void setToken(String token) {
        OAuth2Context.token.set(token);
    }

    static public void initContext(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith(OAuth2AccessToken.BEARER_TYPE + " ")) {
            String token = authorization.substring(7);
            setToken(token);
        }
    }

    static public void cleanContext() {
        token.remove();
        currentUser.remove();
    }

    static public MemberUser getCurrentUser() {
        MemberUser user = currentUser.get();
        if (user == null) {
            String tokenVal = token.get();
            if (tokenVal != null) {
                OAuth2Authentication authentication = tokenStore.readAuthentication(tokenVal);
                user = authentication == null ? null : (MemberUser) authentication.getPrincipal();
                currentUser.set(user);
            }
        }
        return user;
    }

    static public MemberUser reloadCurrentUser() {
        String tokenVal = token.get();
        if (tokenVal != null) {
            OAuth2Authentication authentication = tokenStore.readAuthentication(tokenVal);
            MemberUser user = authentication == null ? null : (MemberUser) authentication.getPrincipal();
            currentUser.set(user);
            return user;
        }
        return null;
    }

    static public MemberUser reloadCurrentUser(String token){
        if (token != null && token.trim().length() > 0) {
            if (token.startsWith(OAuth2AccessToken.BEARER_TYPE + " ")){
                token = token.substring(7);
            }
            OAuth2Authentication authentication = tokenStore.readAuthentication(token);
            MemberUser user = authentication == null ? null : (MemberUser) authentication.getPrincipal();
            currentUser.set(user);
            return user;
        }
        return null;
    }
}
