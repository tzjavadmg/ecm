package com.milisong.upms.utils;


import org.springframework.util.DigestUtils;

/**
 * 签名类 Created by lubao on 2017/9/30.
 */
public class SignatureUtil {

    public static String getSign(String ticket, String appSercet) {
        String origin = ticket + appSercet;
        return DigestUtils.md5DigestAsHex(origin.getBytes());
    }

    public static boolean checkSign(String ticket, String appSercet, String sign) {
        return getSign(ticket, appSercet).equals(sign);
    }
}