package com.milisong.upms.interceptor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.upms.config.FilterUrlConfig;
import com.milisong.upms.constant.SsoConstant;
import com.milisong.upms.constant.SsoErrorConstant;
import com.milisong.upms.service.HttpService;
import com.milisong.upms.utils.SwaggerUrlCheckUtil;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    public static final String METHOD_OPTIONS = "OPTIONS";

    @Autowired
    private FilterUrlConfig filterUrlConfig;
    @Autowired
    private HttpService httpService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (METHOD_OPTIONS.equals(request.getMethod())) {
            return true;
        }
        String requestURI = request.getRequestURI();
        // 如果在白名单中,就通过
        if (!CollectionUtils.isEmpty(filterUrlConfig.getWhitelistUrls())
                && filterUrlConfig.getWhitelistUrls()
                .stream()
                .anyMatch(path -> path.equals(requestURI))) {
            return true;
        }
        
        // swagger不过滤
 		if(SwaggerUrlCheckUtil.check(requestURI)) {
     		return true;
     	}

        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            return noLoginResult(response);
        }
        //校验登陆
        Boolean login = httpService.checkLogin(token);
        if(login==null||!login){
            return noLoginResult(response);
        }
        return true;
    }

    private boolean noLoginResult(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(ResponseResult.buildFailResponse(SsoErrorConstant.SYSTEM_INFO.NOT_LOGIN.getCode(),SsoErrorConstant.SYSTEM_INFO.NOT_LOGIN.getDesc())));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
	 * 判断是否是gsid在url参数里的请求
	 * @param requestUri
	 * @return
	 */
	private boolean checkUrlToken(String requestUri) {
        List<String> notHeadTokenUrls = filterUrlConfig.getNotHeadTokenUrls();
        if(CollectionUtils.isEmpty(notHeadTokenUrls)) {
			return false;
		}
		return notHeadTokenUrls.stream().anyMatch(data -> requestUri.equals(data));
	}

    /**
     * 获取token
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(SsoConstant.TOKEN);
        if(StringUtils.isBlank(token)) {
            if(checkUrlToken(request.getRequestURI())) {
                token = request.getParameter(SsoConstant.TOKEN);
            }
        }
        return token;
    }
}
