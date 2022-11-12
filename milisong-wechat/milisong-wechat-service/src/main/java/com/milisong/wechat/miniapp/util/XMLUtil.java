package com.milisong.wechat.miniapp.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author guohp
 * @version V1.0
 * @Description: xml解析
 * @date 2015-9-22
 */
@Slf4j
public class XMLUtil {

    private static XStream getInstance() {
        XStream xStream  = new XStream(new DomDriver("UTF-8")) {
            /**
             * 忽略xml中多余字段
             */
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };

        // 使用本地的类加载器
        xStream.setClassLoader(XMLUtil.class.getClassLoader());
        // 允许所有的类进行转换
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream;
    }

    public static <T> T convertToObject(String jsonStr, Class<T> clazz) {
        XStream xStream = getInstance();
        // 对指定的类使用Annotations 进行序列化，这步非常关键
        xStream.processAnnotations(clazz);
        T t = (T) xStream.fromXML(jsonStr);
        return t;
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static SortedMap<String, String> doXMLParse(String strxml) {
        if (Strings.isNullOrEmpty(strxml)) {
            return null;
        }
        SortedMap<String, String> m = new TreeMap<String, String>();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List<Element> list = root.getChildren();
            Iterator<Element> it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v = "";
                List<Element> children = e.getChildren();
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = XMLUtil.getChildrenText(children);
                }
                m.put(Underline2Camel.underline2Camel(k, true), v);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("com.tomato.wechat.utils.XMLUtil.doXMLParse", e);
            }
        }
        return m;
    }

    public static SortedMap<String, String> doXMLParse2(String strxml) {
        if (null == strxml || "".equals(strxml.trim())) {
            return null;
        }
        strxml = "<?xml version=\"1.0\" encoding=\"UTF8\"?>" + strxml;
        SortedMap<String, String> m = new TreeMap<String, String>();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List<Element> list = root.getChildren();
            Iterator<Element> it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v = "";
                List<Element> children = e.getChildren();
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = XMLUtil.getChildrenText(children);
                }
                m.put(k, v);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static String StringToXml(Map<String, String> map, String sign) {
        StringBuffer sb = new StringBuffer("<xml>");
        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
        }
        sb.append("<sign>").append(sign).append("</sign>");
        sb.append("</xml>");
        return sb.toString();
    }

    public static String StringToXml(Map<String, String> map) {
        if (null == map) return null;
        StringBuffer sb = new StringBuffer("<xml>");
        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static String wechatCallSuccess() {
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    /***
     * 将金额转换成联动优势对应的金额（联动优势-分）
     *
     * @param transAmt
     * @return
     */
    public static String convertToWxAmt(BigDecimal transAmt) {
        BigDecimal bd2 = new BigDecimal("100");
        double fAmount = transAmt.multiply(bd2).doubleValue();// 转换为以分为单位
        return "" + Math.round(fAmount);
    }

    // public static void main(String[] args) {
    //
    // SortedMap<String, String> signParams = new TreeMap<String, String>();
    // signParams.put("appid", "wx33c6e3a56d5ca153");
    // signParams.put("mch_id", "1487315982"); // 授权后的跳转地址
    // signParams.put("nonce_str", SHA1.getNonceStr());
    // signParams.put("sign_type", "MD5");
    // signParams.put("bill_date", "20170919");
    // signParams.put("bill_type", "ALL");
    // String signValue = SHA1.createSHA1Sign(signParams, "tomatotomatotomatotomatotomatoto");
    // String xmlParam = XMLUtil.StringToXml(signParams, signValue);
    // String result = HttpClientUtil.sendWechatDownloadbillPost("https://api.mch.weixin.qq.com/pay/downloadbill",
    // xmlParam);
    // System.out.println(result);
    // }

}
