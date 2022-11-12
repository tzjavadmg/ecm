package com.tomato.mca.sdk;

import com.tomato.mca.filter.OAuth2ContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;


@Configuration
public class SecurityAutoConfiguration {

    @Bean
    public OAuth2Context oAuth2Context(TokenStore tokenStore) {
        OAuth2Context contextUtils = new OAuth2Context();
        OAuth2Context.setTokenStore(tokenStore);
        return contextUtils;
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OAuth2ContextFilter());
        registration.addUrlPatterns("/*");
        registration.setName("OAuth2ContextFilter");
        registration.setOrder(1);
        return registration;
    }
}
