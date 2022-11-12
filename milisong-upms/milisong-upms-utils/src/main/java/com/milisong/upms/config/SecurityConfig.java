package com.milisong.upms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.milisong.upms.interceptor.LoginInterceptor;
import com.milisong.upms.interceptor.PermissionInterceptor;

/**
 * 权限相关拦截配置
 *
 * @author zhanghuyi
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public PermissionInterceptor PermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
        		.excludePathPatterns("/upms/token")
        		.excludePathPatterns("/actuator/*")
               .addPathPatterns("/**");
        registry.addInterceptor(PermissionInterceptor())
        		.excludePathPatterns("/upms/*")
        		.excludePathPatterns("/actuator/*")
                .addPathPatterns("/**");
    }
}
