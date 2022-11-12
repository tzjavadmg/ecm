package com.milisong.wechat.miniapp.constant;

/**
 * 常量
 */
public class WXConstants {

    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";
    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";
    public enum SignType {
        MD5, HMACSHA256;

        public static SignType convert2Signtype(String signType) {
            if (WXConstants.HMACSHA256.equals(signType)) {
                return HMACSHA256;
            } else if (WXConstants.MD5.equals(signType)) {
                return MD5;
            }
            return null;
        }
    }

}

