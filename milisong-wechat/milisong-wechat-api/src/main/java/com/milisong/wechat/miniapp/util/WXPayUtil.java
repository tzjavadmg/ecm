package com.milisong.wechat.miniapp.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.milisong.wechat.miniapp.config.AppProperties;
import com.milisong.wechat.miniapp.constant.WXConstants;
import com.milisong.wechat.miniapp.dto.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
@Component
public class WXPayUtil {

    private static AppProperties appProperties;
    @Autowired
    private AppProperties miniAppPropertiesAutowired;

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }
    }


    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> fillRequestData(Map<String, String> reqData,Byte buzLine) throws Exception {
        if (null == reqData) {
            reqData = new HashMap<>();
        }

        //小程序
        reqData.put("appid", appProperties.getMiniAppid(buzLine).trim());
        reqData.put("sign_type", WXConstants.MD5);
        reqData.put("mch_id", appProperties.getMchid(buzLine).trim());
        reqData.put("nonce_str", WXPayUtil.generateUUID());
        reqData.put("sign", generateSignature(reqData, appProperties.getApikey(buzLine).trim(), WXConstants.SignType.convert2Signtype(appProperties.getSigntype(buzLine).trim())));
        return reqData;
    }


    public static void fillRequestData(MiniPayRefundRequest request) throws Exception {
        Byte buzLine = request.getBusinessLine();
        request.setAppid(appProperties.getMiniAppid(buzLine).trim());
        request.setSignType(WXConstants.MD5);
        request.setMchId(appProperties.getMchid(buzLine).trim());
        request.setNonceStr(WXPayUtil.generateUUID());
        request.setTotalFee(MiniPayRefundRequest.yuanToFen(request.getTotalAmount()));
        request.setRefundFee(MiniPayRefundRequest.yuanToFen(request.getRefundAmount()));
        //拼团接口回调url使用参数值,兼容老接口
        if(StringUtils.isNotEmpty(request.getNotifyUrl())){
            log.info("使用参数回调notifyURL：{}",request.getNotifyUrl());
            request.setNotifyUrl(request.getNotifyUrl());
        }else {
            request.setNotifyUrl(appProperties.getRefundCallbackUrl(buzLine));
        }
        request.setTotalAmount(null);
        request.setRefundAmount(null);
        request.setBusinessLine(null);
        request.setSign(WXPayUtil.generateSignature(xmlBean2Map(request), appProperties.getApikey(buzLine).trim(), WXConstants.SignType.convert2Signtype(appProperties.getSigntype(buzLine).trim())));
    }

    public static void fillRequestData(MiniBillRequest billRequest) throws Exception {
        Byte buzLine = billRequest.getBusinessLine();
        billRequest.setAppid(appProperties.getMiniAppid(buzLine).trim());
        billRequest.setMchId(appProperties.getMchid(buzLine).trim());
        if ("dev".equalsIgnoreCase(billRequest.getEnv())){
            billRequest.setAppid("wxdc3c4237ba4a4165");
            billRequest.setMchId("1507800561");
        }else if ("test".equalsIgnoreCase(billRequest.getEnv())){
            billRequest.setAppid("wx4ad83b8ee4246b21");
            billRequest.setMchId("1507800561");
        }if ("prod".equalsIgnoreCase(billRequest.getEnv())){
            billRequest.setAppid("wx4ec73a874350fbcc");
            billRequest.setMchId("1505079541");
        }
        billRequest.setSignType(WXConstants.MD5);
        billRequest.setNonceStr(WXPayUtil.generateUUID());
        billRequest.setSign(WXPayUtil.generateSignature(xmlBean2Map(billRequest), appProperties.getApikey(buzLine).trim(), WXConstants.SignType.convert2Signtype(appProperties.getSigntype(buzLine).trim())));
    }

    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, WXConstants.SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data     待签名数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, WXConstants.SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXConstants.FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) { // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        if (WXConstants.SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        } else if (WXConstants.SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        } else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 日志
     *
     * @return
     */
    public static Logger getLogger() {
        Logger logger = LoggerFactory.getLogger("wxpay java sdk");
        return logger;
    }

    /**
     * 获取当前时间戳，单位秒
     *
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     *
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }


    public static List<String> getSuccessBillTypeFieldName(Class<?> cls){

        Set<String> skipFields = Sets.newHashSet();
        skipFields.add("refundId");//微信退款单号
        skipFields.add("outRefundNo");//商户退款单号
        skipFields.add("settlementRefundFee");//退款金额
        skipFields.add("couponRefundFee");//企业红包退款金额
        skipFields.add("refundChannel");//退款类型
        skipFields.add("refundState");//退款状态

        List<String> list = new ArrayList<>();
        // 获取此类所有声明的字段
        Field[] field = cls.getDeclaredFields();
        // 循环此字段数组，获取属性的值
        for (int i = 0; i < field.length && field.length > 0; i++) {
            // 值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查.
            field[i].setAccessible(true);
            // field[i].getName() 获取此字段的名称
            // field[i].get(object) 获取指定对象上此字段的值
            String name = field[i].getName();
            if (name.toLowerCase().contains("serialVersionUID".toLowerCase()) || skipFields.contains(name)){
                //过滤序列化字段
                continue;
            }
            list.add(name);
        }
        return list;
    }

    /**
     * 获取对象中的所有字段名
     *
     * @param cls
     * @return
     */
    public static List<String> getAllBillTypeFieldName(Class<?> cls) {
        List<String> list = new ArrayList<>();
        // 获取此类所有声明的字段
        Field[] field = cls.getDeclaredFields();
        // 循环此字段数组，获取属性的值
        for (int i = 0; i < field.length && field.length > 0; i++) {
            // 值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查.
            field[i].setAccessible(true);
            // field[i].getName() 获取此字段的名称
            // field[i].get(object) 获取指定对象上此字段的值
            String name = field[i].getName();
            if (name.toLowerCase().contains("serialVersionUID".toLowerCase())){
                //过滤序列化字段
                continue;
            }
            list.add(name);
        }
        return list;
    }

    /**
     * 将bean按照@XStreamAlias标识的字符串内容生成以之为key的map对象.
     *
     * @param bean 包含@XStreamAlias的xml bean对象
     * @return map对象 map
     */
    public static Map<String, String> xmlBean2Map(Object bean) {
        Map<String, String> result = Maps.newHashMap();
        List<Field> fields = new ArrayList<>(Arrays.asList(bean.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(bean.getClass().getSuperclass().getDeclaredFields()));
        if (bean.getClass().getSuperclass().getSuperclass() == BaseMiniPayRequest.class) {
            fields.addAll(Arrays.asList(BaseMiniPayRequest.class.getDeclaredFields()));
        }
        for (Field field : fields) {
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                if (field.get(bean) == null) {
                    field.setAccessible(isAccessible);
                    continue;
                }

                if (field.isAnnotationPresent(XStreamAlias.class)) {
                    result.put(field.getAnnotation(XStreamAlias.class).value(), field.get(bean).toString());
                } else if (!Modifier.isStatic(field.getModifiers())) {
                    //忽略掉静态成员变量
                    result.put(field.getName(), field.get(bean).toString());
                }

                field.setAccessible(isAccessible);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }

        }

        return result;
    }

    /**
     *  解析文本账单
     * "所有订单"信息比"成功订单"信息多字段：【微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态】
     * <pr>
     * 交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率
     * 金额,企业红包退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率
     * `2017-09-02 12:15:40,`wx33c6e3a56d5ca153,`1487315982,`0,`,`4000042001201709029708655859,`PO20170902121531757,`oErwd0iU6Sm7sIC9KJ5SrhNhlHgw,`JSAPI,`SUCCESS,`SPDB_DEBIT,`CNY,`3.00,`0.00,`0,`0,`0.00,`0.00,`,`,`购买商品,`,`0.02000,`0.60%
     * 总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额
     * `22,`209.00,`0.00,`0.00,`1.26000
     * </pr>
     *
     * @param rstStr
     * @return
     * @throws Exception
     */
    public static MiniPayBillResult parseBillText(String rstStr, String billType) throws Exception {
        if (null == rstStr || rstStr.trim().length() == 0) {
            return null;
        }
        List<String> list = Splitter.on("\r\n").splitToList(rstStr.replace("`", ""));
        MiniPayBillResult payBillDto = new MiniPayBillResult();

        List<MiniPayBillBaseResult> wxPayBillBaseResultLst = Lists.newArrayList();
        payBillDto.setWxPayBillBaseResultLst(wxPayBillBaseResultLst);


        //第一行为字段描述，倒数一二行是数据汇总和汇总字段描述,最后一行为空白行 T~T
        for (int i = 0; i < list.size(); i++) {
            if (i == 0 || i == list.size() - 3) {
                //第一行和倒数第二行是描述，跳过
                continue;
            }
            List<String> billDataListStr = Splitter.on(",").splitToList(list.get(i));
            if(billDataListStr.size() == 1){
                continue;
            }
            if (i == list.size() - 2) {
                //最后一行是数据汇总
                payBillDto.setTotalRecord(billDataListStr.get(0));//总交易单数
                payBillDto.setTotalFee(billDataListStr.get(1));//总交易额
                payBillDto.setTotalRefundFee(billDataListStr.get(2));//总退款金额
                payBillDto.setTotalCouponFee(billDataListStr.get(3));//总代金券立减优惠退款金额
                payBillDto.setTotalPoundageFee(billDataListStr.get(4));//手续费总金额
            }else {
                MiniPayBillBaseResult wxPayBillBase = setFieldValue(MiniPayBillBaseResult.class,billDataListStr.toArray(new String[billDataListStr.size()]),billType);
                wxPayBillBaseResultLst.add(wxPayBillBase);
            }
        }
        return payBillDto;
    }

    public static <T> T setFieldValue(Class<T> beanClass, String[] values,String billType) {
        T bean = null;
        Field field = null;
        try {
            List<String> fieldNamList = Lists.newArrayList();
            fieldNamList = getAllBillTypeFieldName(beanClass);
//            switch (billType){
//                case "All":
//                    log.info("ALLLLLLLLLLLLLLLL");
//                    fieldNamList = getAllBillTypeFieldName(beanClass);
//                    break;
//                case "SUCCESS":
//                    log.info("SUCCESSSSSSSSSSSSS");
//                    fieldNamList = getSuccessBillTypeFieldName(beanClass);
//                    break;
//            }
            // 获得对象实例
            bean = beanClass.newInstance();
            for (int i = 0; i < fieldNamList.size(); i++) {
                // 获得
                field = beanClass.getDeclaredField(fieldNamList.get(i));
                if (field != null) {
                    field.setAccessible(true);
                    // 赋值给bean对象对应的值
                    field.set(bean, values[i]);
                }
            }
        } catch (InstantiationException e) {
            log.error("",e);
        } catch (IllegalAccessException e) {
            log.error("",e);
        } catch (NoSuchFieldException e) {
            log.error("",e);
        }
        return bean;
    }

    public static void fillRequestData(MiniRefundQueryRequest refundQueryRequest) throws Exception {
        Byte buzLine = refundQueryRequest.getBusinessLine();
        //小程序
        refundQueryRequest.setAppid(appProperties.getMiniAppid(buzLine).trim());
        refundQueryRequest.setSignType(WXConstants.MD5);
        refundQueryRequest.setMchId(appProperties.getMchid(buzLine).trim());
        refundQueryRequest.setNonceStr(WXPayUtil.generateUUID());
        refundQueryRequest.setSign(WXPayUtil.generateSignature(xmlBean2Map(refundQueryRequest), appProperties.getApikey(buzLine).trim(), WXConstants.SignType.convert2Signtype(appProperties.getSigntype(buzLine).trim())));
    }

    @PostConstruct
    public void autowired() {
        appProperties = miniAppPropertiesAutowired;
    }

}
