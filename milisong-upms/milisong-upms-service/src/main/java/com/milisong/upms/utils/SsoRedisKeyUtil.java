package com.milisong.upms.utils;

/**
 * @author zhaozhonghui
 * @date 2018-11-07
 */
public class SsoRedisKeyUtil {
    public static final String EMPTY = "empty";
    
    // 私有全局key
    private final static String SSO_KEY = "sso:";
    private final static String TOKEN_2_GSID_KEY = "t_2_g:";
    private final static String GSID_2_TOKEN_KEY = "g_2_t:";
    private final static String TOKEN_2_APP = "t_2_a:";
    
    public final static String USER_INFO = "user";
    public final static String PERMISSION = "permission";
    public final static String DATA_PERMISSION = "data_permission";
    public final static String MENU = "menu";
    
    /**
     * 得到sso的用户权限相关的Hash Key
     * @param token
     * @return
     */
    public static String getSsoHashKey(String token) {
    	return SSO_KEY.concat(token);
    }
    
    /**
     * 得到token和gsid的对应关系的redis key
     * @param token
     * @return
     */
    public static String getToken2GsidKey(String token) {
    	return TOKEN_2_GSID_KEY.concat(token);
    }
    
    /**
     * 得到gsid和token的对应关系的redis key
     * @param gsid
     * @return
     */
    public static String getGsid2TokenKey(String gsid) {
    	return GSID_2_TOKEN_KEY.concat(gsid);
    }
    
    /**
     * 得到token对应的系统名称
     * @param token
     * @return
     */
    public static String getToken2AppNameKey(String token) {
    	return TOKEN_2_APP.concat(token);
    }
}
