package com.milisong.ucs.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/8/31 下午5:51
 * @description
 */
@Data
public class UserRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = -3733887361580797081L;
    private Long userId;

    private String nickName;

    private String headPortraitUrl;

    private String openid;

    private String registerQrcode;//二维码code

    private String jsCode;//js登录时获取的 code

    private String encryptedData;//包括敏感数据在内的完整用户信息的加密数据，详细见加密数据解密算法

    private String iv;//加密算法的初始向量，详细见加密数据解密算法

    private String formId;//用户触发的表单id，用户发送模板消息

    private String sessionkey;

    private Integer loginSource;//登录来源，0 小程序首页登录，1 多人分享红包登录

    private Long shopId;
}