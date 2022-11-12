package com.milisong.wechat.miniapp.dto;

import com.milisong.wechat.miniapp.annotation.Required;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sailor wang
 * @date 2018/12/1 6:45 PM
 * @description
 */
@Setter
@Getter
public abstract class BaseDto {
    @Required
    private Byte businessLine;
}