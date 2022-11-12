package com.milisong.wechat.miniapp.dto;

import lombok.Data;

/**
 * @author sailor wang
 * @date 2018/8/31 下午9:45
 * @description
 */
@Data
public abstract class BaseWechat {
    private String errorcode;
    private String errormsg;
}