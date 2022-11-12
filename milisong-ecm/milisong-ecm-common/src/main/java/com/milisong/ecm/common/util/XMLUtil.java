package com.milisong.ecm.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;


@Slf4j
public class XMLUtil {

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static SortedMap<String, String> doXMLParse(String strxml) {
        if (null == strxml || "".equals(strxml)) {
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
                m.put(k, v);
            }
        } catch (Exception e) {
            log.error("", e);
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

}
