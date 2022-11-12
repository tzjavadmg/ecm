package com.milisong.wechat.miniapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MiniMsgTemplateDto extends BaseDto {
    // 消息类型 1 支付成功 2 支付失败
    private String msgId;
    private String openId;
    private List<String> data;
    private String formId;
    private String transferPage;



}
