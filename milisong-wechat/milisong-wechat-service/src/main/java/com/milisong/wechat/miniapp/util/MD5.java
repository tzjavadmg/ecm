package com.milisong.wechat.miniapp.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author WangXuelin
 * @version 1.0.0
 * @description MD5加密解密
 * @date 2014-7-9
 */
@Slf4j
public class MD5 {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * J 转换byte到16进制
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * J 编码
     *
     * @param origin 需加密的字符串
     * @return String 加密后的字符串
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }

    /**
     * MessageDigest 为 JDK 提供的加密类
     *
     * @param bytes 需加密的字节数组
     * @return String 加密后的字符串
     */
    public static String MD5Encode(byte[] bytes) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(bytes));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * 通过特定编码格式加密字符串
     *
     * @param origin      需加密的字符串
     * @param charsetName 编码格式
     * @return String 加密后的字符串
     */
    public static String MD5Encode(String origin, String charsetName) {
        origin = origin.trim();
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
        } catch (Exception ex) {
        }
        return resultString;
    }

    // 创建签名
    public static String getSign(Map<String, Object> signParams, String key) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> es = signParams.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            sb.append(k + "=" + v + "&");
            // 要采用URLENCODER的原始值！
        }

        sb.append("key=").append(key);
        String params = sb.toString();
        log.info("参数拼接：" + params);
        String sign = MD5Encode(params, "UTF-8").toUpperCase();
        return sign;
    }

//    public static void main(String[] args) {
//        System.out.println(MD5.MD5Encode("123123abc"));
//    }

}
