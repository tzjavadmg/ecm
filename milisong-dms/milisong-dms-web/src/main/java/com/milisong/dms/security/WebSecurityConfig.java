//package com.milisong.dms.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//
//import com.milisong.dms.sso.interceptor.SecurityInterceptor;
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
//        registry.addInterceptor(getSecurityInterceptor())
//                .excludePathPatterns("/file/upload")
//                .excludePathPatterns("/sso/menu")
//        		.excludePathPatterns("/order-set/update-status")
//        		.excludePathPatterns("/order-set/query-detail-order-info")
//        		.excludePathPatterns("/order-set/query-detail-order-set-status")
//        		.excludePathPatterns("/shop/get-display-info-byCode")
//        		.excludePathPatterns("/shop/get-display-info-byBuildingId")
//        		.excludePathPatterns("/shop/get-all-shop-list")
//        		.excludePathPatterns("/shop/save-or-update")
//        		.excludePathPatterns("/shop/query-by-condition")
//        		.excludePathPatterns("/shop/query-by-id")
//        		.excludePathPatterns("/company/sync")
//        		.excludePathPatterns("/company/update-floor")
//        		.excludePathPatterns("/goods/task")
//        		.excludePathPatterns("/orderset/task")
//        		.excludePathPatterns("/orderset/destroy-pool")
//        		.excludePathPatterns("/orderset/compensate-send-mq")
//				.excludePathPatterns("/shunfeng/task/**")
//				.excludePathPatterns("/shunfeng/back/order-status")
//				.excludePathPatterns("/shunfeng/create")
//        		.excludePathPatterns("/config/**")
//        		.excludePathPatterns("/stock/get-goods-pre-production")
//        		.excludePathPatterns("/stock/get-day-goods-pre-production")
//        		.excludePathPatterns("/building/get-building-by-id")
//                .addPathPatterns("/**");
//    }
//}
