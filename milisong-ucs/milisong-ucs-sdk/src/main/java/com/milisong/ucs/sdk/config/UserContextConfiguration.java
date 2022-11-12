package com.milisong.ucs.sdk.config;

import com.milisong.ucs.api.UserService;
import com.milisong.ucs.sdk.security.InitUserContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserContextConfiguration {

    @Autowired
    UserService userService;

    @Bean
    public FilterRegistrationBean oAuth2ContextFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        InitUserContextFilter initUserContextFilter = new InitUserContextFilter();
        initUserContextFilter.setUserService(userService);
        registration.setFilter(initUserContextFilter);
        registration.addUrlPatterns("/*");
        registration.setName("InitUserContextFilter");
        registration.setOrder(1);
        return registration;
    }
}
