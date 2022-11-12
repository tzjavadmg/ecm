package com.milisong.wechat.miniapp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class SHA1 {

    /**
     * 串接arr参数，生成sha1 digest
     */
    public static String gen(String... arr) {
        if (StringUtils.isAnyEmpty(arr)) {
            throw new IllegalArgumentException("非法请求参数，有部分参数为空 : " + Arrays.toString(arr));
        }

        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String a : arr) {
            sb.append(a);
        }
        return DigestUtils.sha1Hex(sb.toString());
    }

    /**
     * 用&串接arr参数，生成sha1 digest
     */
    public static String genWithAmple(String... arr) {
        if (StringUtils.isAnyEmpty(arr)) {
            throw new IllegalArgumentException("非法请求参数，有部分参数为空 : " + Arrays.toString(arr));
        }

        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String a = arr[i];
            sb.append(a);
            if (i != arr.length - 1) {
                sb.append('&');
            }
        }
        log.info("SHA1加密字符串：" + sb.toString());
        return DigestUtils.sha1Hex(sb.toString());
    }

    public static String getNonceStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date()) + RandomUtils.getRandomString(5, 4);
    }

    public static String createSHA1Sign(SortedMap<String, String> signParams, String key) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = signParams.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            sb.append(k + "=" + v + "&");
            // 要采用URLENCODER的原始值！
        }
        if (key != null) {
            sb.append("key=").append(key);
        } else {
            return sha1(sb.deleteCharAt(sb.length() - 1).toString());
        }
        String params = sb.toString();
        log.info("参数拼接：" + params);
        String sign = MD5.MD5Encode(params, "UTF-8").toUpperCase();
        return sign;
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static String sha1(String params) {
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(params.getBytes("UTF-8"));
            String signature = byteToHex(crypt.digest());
            return signature;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
