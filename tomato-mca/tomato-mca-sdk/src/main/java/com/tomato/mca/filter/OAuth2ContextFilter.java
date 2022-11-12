package com.tomato.mca.filter;

import com.tomato.mca.sdk.OAuth2Context;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
public class OAuth2ContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        try {
            if (log.isDebugEnabled()) {
                log.debug("OAuth2Context 开始初始化....................................." + request.getRequestURI() + "?" + request.getQueryString());
            }
            OAuth2Context.initContext(request);
            if (log.isDebugEnabled()) {
                log.debug("OAuth2Context 初始化完成....................................." + OAuth2Context.getToken());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }finally{
            OAuth2Context.cleanContext();
            if (log.isDebugEnabled()) {
                log.debug("OAuth2Context 销毁成功.....................................");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
