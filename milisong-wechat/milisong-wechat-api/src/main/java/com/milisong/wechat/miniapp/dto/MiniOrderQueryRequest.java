package com.milisong.wechat.miniapp.dto;

import com.milisong.wechat.miniapp.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/12/1 6:52 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniOrderQueryRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = -3140578317462424782L;

    @Required
    private String outTradeNo;

}