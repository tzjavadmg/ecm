package com.milisong.ucs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/8/31 下午5:42
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -5775928059130355156L;

    private Long id;

    private String realName;

    private String sessionKey;

    // 会话密钥
    private String wxSessionKey;

    private String openId;

    private Integer registerSource;

    private String wechatContractNo;

    private String mobileNo;

    private String nickName;

    private String headPortraitUrl;

    private Date birthday;

    private Integer sex;

    //包括敏感数据在内的完整用户信息的加密数据，详细见加密数据解密算法
    private String encryptedData;

    //加密算法的初始向量，详细见加密数据解密算法
    private String iv;

    //用户触发的表单id，用户发送模板消息
    private String formId;

    private Date createTime;

    private Date updateTime;

    //是否新用户
    private Byte isNew;

    //是否领取过新人红包
    private Byte receivedNewRedPacket;

    //是否领取过早餐新人券
    private Byte receivedNewCoupon;

    private String traceId;

    private Integer fetchSize;

    private List<Long> ids;

    //引导小程序弹层
    private AddMiniAppTips addMiniAppTips;

    // 积分流水产生类型 UserPointEnum#Action  1 支付订单 , 2 订单完成 , 3 订单退款 , 4 取消订单
    private Byte userPointAction;

    // 可用积分
    private Integer usefulPoint;

    // 已用积分
    private Integer usedPoint;

    // 幂等标识
    private String idempotent;

    // 手机号
    private List<String> mobileNos;

    //小程序分享图地址，名字保持和之前命名保持一致
    private String pictureUrl;

    //小程序分享标题，名字保持和之前命名保持一致
    private String title;
}