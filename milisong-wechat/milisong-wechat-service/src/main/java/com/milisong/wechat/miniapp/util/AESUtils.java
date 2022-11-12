package com.milisong.wechat.miniapp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


/**
 * Created by zhanghuyi on 2018/6/28.
 */
@Slf4j
public class AESUtils {

    public static String decryptForMini(String encryptedData, String sessionKey, String iv) {
        try {
            byte[] bytes = Base64.decodeBase64(encryptedData);
            IvParameterSpec ivSpec = new IvParameterSpec(Base64.decodeBase64(iv));
            Key key = new SecretKeySpec(Base64.decodeBase64(sessionKey), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] ret = cipher.doFinal(bytes);
            return new String(ret, "UTF-8");
        } catch (Exception e) {
            log.error("mini AES解密失败 " + e.getMessage(), e);
        }
        return "";
    }


    public static void main(String[] args) {
        System.out.println(decryptForMini(
                "fKomGXyR2MjdJaKRLYwiwZV92tx4wZkyupDdKhblWKakC/p7CixK2ZaBUE3zG6uQKuzpYH9K3eHCmjhi8A46R2biRuhAFbQ40/doC3mRKqOT876ryzNa95cVeno2WMJ+HaywwRbofb3K+2HwSQSSUNXyEPraeVlTSiF65TcmG7jUr3ZieqBgSUn2PqFnvdpw8oUW6mHX9xk5WeLcCeEEDgizgL8qWkA3tEYfQBoWd3z9fZUZvXGOn+HWBHLH0BPembdOw4RDhFdqLxyimHXnK2cv3rEXPH//Ep7CaYt8nNJrpXk0UnOXlgmhnjKUg1khIaQJA9HJrPb/mc9p6LYtLjyI3APyO3CXI2Dv1eG3wTZ4l6nkOICClXXWNTaYsQ6OtL4yPArThmn8czOH5ei4zOdZziLKM70oToZYLKW/wDfoK0hSEGRFek0R/gQ1GqKINwg72pfkyO4VETipnN91s6WmO4j8slt0JRQPksmbT1k=",
                "wkHp1+nhfgERrBWmLLG0gA==",
                "wRTJIYoPmKJ1fSvFBmPiXA=="));
    }
}
