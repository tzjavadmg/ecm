package com.milisong.wechat.miniapp.dto;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author sailor wang
 * @date 2018/10/9 下午4:24
 * @description
 */
@Data
public abstract class BaseMiniPayRequest implements Serializable {
    private static final long serialVersionUID = -4766915659779847060L;

    /**
     * <pre>
     * 字段名：微信分配小程序ID
     * 变量名：appid
     * 是否必填：是
     * 类型：String(32)
     * 示例值：wxd678efh567hg6787
     * 描述：微信分配的公众账号ID（企业号corpid即为此appId）
     * </pre>
     */
    @XStreamAlias("appid")
    protected String appid;
    /**
     * <pre>
     * 字段名：微信商户号.
     * 变量名：mch_id
     * 是否必填：是
     * 类型：String(32)
     * 示例值：1230000109
     * 描述：微信支付分配的商户号
     * </pre>
     */
    @XStreamAlias("mch_id")
    protected String mchId;

    /**
     * <pre>
     * 字段名：随机字符串.
     * 变量名：nonce_str
     * 是否必填：是
     * 类型：String(32)
     * 示例值：5K8264ILTKCH16CQ2502SI8ZNMTM67VS
     * 描述：随机字符串，不长于32位。推荐随机数生成算法
     * </pre>
     */
    @XStreamAlias("nonce_str")
    protected String nonceStr;
    /**
     * <pre>
     * 字段名：签名.
     * 变量名：sign
     * 是否必填：是
     * 类型：String(32)
     * 示例值：C380BEC2BFD727A4B6845133519F3AD6
     * 描述：签名，详见签名生成算法
     * </pre>
     */
    @XStreamAlias("sign")
    protected String sign;

    /**
     * <pre>
     * 签名类型.
     * sign_type
     * 否
     * String(32)
     * HMAC-SHA256
     * 签名类型，目前支持HMAC-SHA256和MD5
     * </pre>
     */
    @XStreamAlias("sign_type")
    private String signType;

    private Byte businessLine;

    /**
     * 将单位为元转换为单位为分.
     *
     * @param yuan 将要转换的元的数值字符串
     * @return the integer
     */
    public static Integer yuanToFen(BigDecimal yuan) {
        return yuan.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 检查约束情况.
     *
     */
    protected abstract String checkConstraints();

    /**
     * 如果配置中已经设置，可以不设置值.
     *
     * @param appid 微信公众号appid
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * 如果配置中已经设置，可以不设置值.
     *
     * @param mchId 微信商户号
     */
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     * 默认采用时间戳为随机字符串，可以不设置.
     *
     * @param nonceStr 随机字符串
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    /**
     * To xml string.
     *
     * @return the string
     */
    public String toXML() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(this.getClass());
        return xstream.toXML(this);
    }

}
