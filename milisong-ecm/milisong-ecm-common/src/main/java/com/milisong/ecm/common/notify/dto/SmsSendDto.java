package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.util.List;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/22   17:51
 *    desc    : 短信发送入参dto
 *    version : v1.0
 * </pre>
 */
@Data
public class SmsSendDto {
    private List<String> mobile;
    private String templateCode;
    private List<String> params;
    private Byte businessLine;

    private Boolean advFlag = false;
}
