package com.milisong.ucs.sdk.security;


import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
public class InitUserContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    UserService userService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        String session = request.getHeader("Authorization");
        initUserInfo(session);
        String url=request.getRequestURI();
        log.info("Authorization -> {}, 初始化UserSession过滤器拦截 url -> {}",session,url);

        if(!"/health".equals(url) && !"/trace".equals(url) && !"/info".equals(url) && !"/dump".equals(url)) {
            try {
                UserContext.initContext(request);
                filterChain.doFilter(servletRequest, servletResponse);
            } finally {
                UserContext.cleanContext();
            }
        }
    }

    private void initUserInfo(String session){
        if (StringUtils.isNotBlank(session)){
            UserDto userDto = new UserDto();
            userDto.setSessionKey(session);
            ResponseResult<UserDto> result = userService.getUserInfo(userDto);
            log.info("根据Authorization获取用户数据, session -> {}, user -> {}",session,result.getData());
            if (result.isSuccess() && result.getData() != null){
                UserSession userSession = BeanMapper.map(result.getData(), UserSession.class);
                UserContext.loginSession(userSession);
            }
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void destroy() {

    }
}
