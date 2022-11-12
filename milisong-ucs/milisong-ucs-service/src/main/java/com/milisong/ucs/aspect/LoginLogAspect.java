package com.milisong.ucs.aspect;

import com.farmland.core.db.IdGenerator;
import com.milisong.ucs.domain.LoginLog;
import com.milisong.ucs.dto.UserRequest;
import com.milisong.ucs.mapper.LoginLogMapper;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 记录登录日志
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Slf4j
@Aspect
@Component
public class LoginLogAspect {

    @Autowired
    LoginLogMapper loginLogMapper;


    @Pointcut("@annotation(com.milisong.ucs.annotation.LoginLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 执行方法
        Object result = point.proceed();
        try {
            Object[] args = point.getArgs();
            Byte buzline = null;
            if (args != null) {
                for (Object arg : args) {
                    try {
                        if (arg instanceof UserRequest) {
                            UserRequest request = (UserRequest) arg;
                            buzline = request.getBusinessLine();
                            break;
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            }
            //保存日志
            saveLog(buzline);
        } catch (Exception e) {
            log.error("保存登录日志异常",e);
        }
        return result;
    }

    void saveLog(Byte buzline)  {
        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        LoginLog loginLog = new LoginLog();
        loginLog.setId(IdGenerator.nextId());
        loginLog.setUserId(UserContext.getCurrentUser().getId());
        loginLog.setNickName(UserContext.getCurrentUser().getNickName());
        loginLog.setMobileNo(UserContext.getCurrentUser().getMobileNo());
        loginLog.setOpenId(UserContext.getCurrentUser().getOpenId());
        loginLog.setBusinessLine(buzline);
        loginLog.setSession(UserContext.getSession());
        loginLog.setIpAddr(IPUtils.getIpAddr(request));
        loginLog.setClientType(UserContext.getCurrentUser().getRegisterSource().toString());
        Date now = new Date();
        loginLog.setCreateTime(now);
        loginLog.setUpdateTime(now);

        // 保存系统日志
        loginLogMapper.insertSelective(loginLog);
    }
}
