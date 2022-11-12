package com.milisong.wechat.miniapp.util;

import com.milisong.wechat.miniapp.enums.BusinessLine;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;

/**
 * 获取微信apiclient_cert.p12证书
 *
 * @author sailor wang
 * @date 2018/10/10 下午3:09
 * @description
 */
@Slf4j
@Component
public class CertHttpUtil {

    // 连接超时时间，默认10秒
    private static int socketTimeout = 10000;
    // 传输超时时间，默认30秒
    private static int connectTimeout = 30000;
    // 根据默认超时限制初始化requestConfig，请求器的配置
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

    //午餐证书
    private static File LUNCH_CERT;

    //早餐证书
    private static File BREAKFAST_CERT;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 通过Https往API post xml数据
     *
     * @param url     API地址
     * @param xmlObj  要提交的XML数据对象
     * @param mchId   商户ID
     * @param buzLine 业务线
     * @return
     */
    public static String postData(String url, String xmlObj, String mchId, Byte buzLine) {
        HttpPost httpPost = new HttpPost(url);
        try (CloseableHttpClient httpClient = initCertHttpClient(mchId, buzLine)){
            // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
            StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(postEntity);
            // 设置请求器的配置
            httpPost.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            log.error("",e);
        } finally {
            httpPost.abort();
        }
        return null;
    }

    /**
     * 加载证书
     *
     * @param mchId   商户ID
     * @param buzLine 业务线
     * @throws Exception
     */
    private static CloseableHttpClient initCertHttpClient(String mchId, Byte buzLine) throws Exception {
        // 证书密码，默认为商户ID
        String key = mchId;
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 读取本机存放的PKCS12证书文件
        FileInputStream instream = null;
        if (BusinessLine.BREAKFAST.getCode().equals(buzLine)) {
            instream = new FileInputStream(BREAKFAST_CERT);
        } else {
            instream = new FileInputStream(LUNCH_CERT);
        }
        try {
            // 指定PKCS12的密码(商户ID)
            keyStore.load(instream, key.toCharArray());
        } finally {
            if (instream != null) {
                instream.close();
            }
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    @PostConstruct
    public void autowired(){
        try {
            LUNCH_CERT = File.createTempFile("apiclient_cert", ".p12");
            Resource resource = new ClassPathResource("cert/" + env + "/apiclient_cert.p12");
            inputstreamToFile(resource.getInputStream(), CertHttpUtil.LUNCH_CERT);

            BREAKFAST_CERT = File.createTempFile("breakfast_cert", ".p12");
            Resource breakfastResource = new ClassPathResource("cert/" + env + "/breakfast_cert.p12");
            inputstreamToFile(breakfastResource.getInputStream(), CertHttpUtil.BREAKFAST_CERT);
        } catch (IOException e) {
        }
    }

    private static void inputstreamToFile(InputStream ins, File file) throws IOException {
        if (file == null) {
            file = new File("", "");
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
}