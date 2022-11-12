package com.tomato.mca.interceptor;

import com.tomato.mca.sdk.SecurityContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * 名    称：
 * 功    能：将token透传给feign client
 * 创 建 人：sailor
 * 创建时间：2017/10/29下午10:18
 * 修 改 人：
 * 修改时间：
 * 说    明：
 * 版 本 号：
 */
@Configuration
@Slf4j
public class OauthFeignServerInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = null;
        try {
            token = SecurityContext.getToken();
        } catch (Exception e) {
        }
        if (log.isDebugEnabled()) {
            log.debug("微服务token透传，token -> {} ", token);
        }
        if (token != null && token.trim().length() > 0){
            requestTemplate.header("Authorization", OAuth2AccessToken.BEARER_TYPE+" "+token);
        }
    }
}
