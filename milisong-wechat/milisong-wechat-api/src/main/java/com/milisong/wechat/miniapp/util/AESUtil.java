package com.milisong.wechat.miniapp.util;

import com.milisong.wechat.miniapp.config.LanuchAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

@Slf4j
@Component
public class AESUtil {

    private static LanuchAppProperties miniAppProperties;
    @Autowired
    private LanuchAppProperties miniAppPropertiesAutowired;

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";


    //对商户key做md5，得到32位小写key*
    private static SecretKeySpec key = null;

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64Util.encode(cipher.doFinal(data.getBytes()));
    }

    /**
     * AES解密
     * <p>
     * （1）对加密串A做base64解码，得到加密串B
     * （2）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）
     *
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64Util.decode(base64Data)));
    }

    public static void main(String[] args) throws Exception {
        String A = "小程序返回的加密信息req_info";
        System.out.println(AESUtil.decryptData(A));
    }

    @PostConstruct
    public void autowired() {
        try {
            miniAppProperties = miniAppPropertiesAutowired;
            key = new SecretKeySpec(MD5Util.MD5Encode(miniAppProperties.getApikey(), "UTF-8").toLowerCase().getBytes(), ALGORITHM);
        } catch (Exception e) {

        }
    }
}