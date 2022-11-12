package com.milisong.ucs.sdk.security;

import com.farmland.core.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserContext {

    private final static String LOGIN_SESSION = "session:%s";
    private final static int LOGIN_SESSION_EXPIRE = 30;

    private final static ThreadLocal<String> SESSION = new ThreadLocal<String>();

    private final static ThreadLocal<UserSession> CURRENT_USER = new ThreadLocal<UserSession>();


    static public String getSession() {
        return SESSION.get();
    }

    static public void setSession(String sessionKey) {
        UserContext.SESSION.set(sessionKey);
    }

    static public void setCurrentUser(UserSession user) {
        UserContext.CURRENT_USER.set(user);
    }

    static public void initContext(HttpServletRequest request) {
        String session = request.getHeader("Authorization");
        if (session != null) {
            setSession(String.format(LOGIN_SESSION, session));
        }
    }

    static public void cleanContext() {
        SESSION.remove();
        CURRENT_USER.remove();
    }

    static public UserSession getCurrentUser() {
        UserSession user = CURRENT_USER.get();
        if (user == null) {
            String session = SESSION.get();
            if (session != null) {
                Map<Object, Object> userMap = RedisCache.hGetAll(session);
                user = UserSession.toUserSession(userMap);
                CURRENT_USER.set(user);
            }
        }
        return user;
    }

    static public void freshUser(String openid, Integer registerSource,Byte buzLine, String key, Object val) {
        if (openid != null && registerSource != null && buzLine != null && key != null) {
            String sessionKey = String.format(LOGIN_SESSION, openid + registerSource + buzLine);
            RedisCache.getRedisTemplate().opsForHash().put(sessionKey, key, val);
            RedisCache.getRedisTemplate().expire(sessionKey, LOGIN_SESSION_EXPIRE, TimeUnit.DAYS);
            loadCurrentUser();
        }
    }

    static public Map<Object, Object> getUserBySession(String session) {
        if (StringUtils.isNotBlank(session)) {
            String sessionKey = String.format(LOGIN_SESSION, session);
            Map<Object, Object> userMap = RedisCache.hGetAll(sessionKey);
            return userMap;
        }
        return null;
    }

    static public void freshUser(UserSession userSession) {
        String openId = userSession.getOpenId();
        Integer registerSource = userSession.getRegisterSource();
        Byte buzLine = userSession.getBusinessLine();
        String sessionKey = String.format(LOGIN_SESSION, openId + registerSource+buzLine);
        RedisCache.hPutAll(sessionKey, userSession.toSessionMap());
        RedisCache.getRedisTemplate().expire(sessionKey, LOGIN_SESSION_EXPIRE, TimeUnit.DAYS);
        loadCurrentUser();
    }

    static public UserSession loadCurrentUser() {
        String session = SESSION.get();
        if (session != null) {
            Map<Object, Object> userMap = RedisCache.hGetAll(session);
            UserSession user = UserSession.toUserSession(userMap);
            CURRENT_USER.set(user);
            return user;
        }
        return null;
    }


    public static String loginSession(UserSession user) {
        String buzLine = "";
        if (user.getBusinessLine() != null){
            buzLine = user.getBusinessLine().toString();
        }
        String sessionKey = String.format(LOGIN_SESSION, user.getOpenId() + user.getRegisterSource() + buzLine);
        RedisCache.hPutAll(sessionKey, user.toSessionMap());
        RedisCache.getRedisTemplate().expire(sessionKey, LOGIN_SESSION_EXPIRE, TimeUnit.DAYS);

        setCurrentUser(user);
        setSession(sessionKey);
        return user.getOpenId() + user.getRegisterSource() + buzLine;
    }

}
