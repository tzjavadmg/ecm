package com.milisong.wechat.miniapp.dto;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.milisong.wechat.miniapp.annotation.Required;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public abstract class BasicPayDto implements Serializable{

    /**
     * 返回状态码
     */
    @XStreamAlias("return_code")
    protected String returnCode;
    /**
     * 返回信息
     */
    @XStreamAlias("return_msg")
    protected String returnMsg;

    //当return_code为SUCCESS的时候，还会包括以下字段：

    /**
     * 业务结果
     */
    @XStreamAlias("result_code")
    private String resultCode;
    /**
     * 错误代码
     */
    @XStreamAlias("err_code")
    private String errCode;
    /**
     * 错误代码描述
     */
    @XStreamAlias("err_code_des")
    private String errCodeDes;
    /**
     * 公众账号ID
     */
    @XStreamAlias("appid")
    private String appid;
    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mchId;
    /**
     * 服务商模式下的子公众账号ID
     */
    @XStreamAlias("sub_appid")
    private String subAppId;
    /**
     * 服务商模式下的子商户号
     */
    @XStreamAlias("sub_mch_id")
    private String subMchId;
    /**
     * 随机字符串
     */
    @XStreamAlias("nonce_str")
    private String nonceStr;
    /**
     * 签名
     */
    @XStreamAlias("sign")
    private String sign;

    private String timeStamp;

    //以下为辅助属性
    /**
     * xml字符串
     */
    private String xmlString;

    /**
     * xml的Document对象，用于解析xml文本
     */
    private Document xmlDoc;

    @Required
    private Byte businessLine;

    /**
     * 将单位分转换成单位圆
     *
     * @param fee 将要被转换为元的分的数值
     */
    public static String feeToYuan(Integer fee) {
        return new BigDecimal(Double.valueOf(fee) / 100).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 从xml字符串创建bean对象
     */
    public static <T extends BasicPayDto> T fromXML(String xmlString, Class<T> clz) {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(clz);
        T result = (T) xstream.fromXML(xmlString);
        result.setXmlString(xmlString);
        return result;
    }

    /**
     * 将bean通过保存的xml字符串转换成map
     */
    public Map<String, String> toMap() {
        if (StringUtils.isBlank(this.xmlString)) {
            throw new RuntimeException("xml数据有问题，请核实！");
        }

        Map<String, String> result = Maps.newHashMap();
        Document doc = this.getXmlDoc();

        try {
            NodeList list = (NodeList) XPathFactory.newInstance().newXPath()
                    .compile("/xml/*")
                    .evaluate(doc, XPathConstants.NODESET);
            int len = list.getLength();
            for (int i = 0; i < len; i++) {
                result.put(list.item(i).getNodeName(), list.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException("非法的xml文本内容：" + xmlString);
        }

        return result;
    }

    /**
     * 将xml字符串转换成Document对象，以便读取其元素值
     */
    protected Document getXmlDoc() {
        if (this.xmlDoc != null) {
            return this.xmlDoc;
        }

        try {
            this.xmlDoc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(this.xmlString.getBytes("UTF-8")));
            return xmlDoc;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException("非法的xml文本内容：" + this.xmlString);
        }

    }

    /**
     * 获取xml中元素的值
     */
    protected String getXmlValue(String... path) {
        Document doc = this.getXmlDoc();
        String expression = String.format("/%s//text()", Joiner.on("/").join(path));
        try {
            return (String) XPathFactory
                    .newInstance()
                    .newXPath()
                    .compile(expression)
                    .evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("未找到相应路径的文本：" + expression);
        }
    }

    /**
     * 获取xml中元素的值，作为int值返回
     */
    protected Integer getXmlValueAsInt(String... path) {
        String result = this.getXmlValue(path);
        if (StringUtils.isBlank(result)) {
            return null;
        }

        return Integer.valueOf(result);
    }
}
