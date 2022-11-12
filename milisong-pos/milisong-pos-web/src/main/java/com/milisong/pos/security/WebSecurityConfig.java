//package com.milisong.pos.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//
//import com.milisong.scm.sso.interceptor.SecurityInterceptor;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebSecurityConfig implements WebMvcConfigurer {
//
//    @Bean
//    public SecurityInterceptor getSecurityInterceptor() {
//        return new SecurityInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        /*registry.addInterceptor(getSecurityInterceptor())
//                .excludePathPatterns("/preproduction/task")
//                .addPathPatterns("/**");*/
//    }
//}