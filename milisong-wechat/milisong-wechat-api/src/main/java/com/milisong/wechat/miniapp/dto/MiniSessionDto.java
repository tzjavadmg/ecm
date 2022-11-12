package com.milisong.wechat.miniapp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
/**
 * @author sailor wang
 * @date 2018/8/31 下午2:45
 * @description
 */
public class MiniSessionDto extends BaseWechat {

    private String sessionKey;

    private String openid;

    private String unionid;

    private String errcode;

}
