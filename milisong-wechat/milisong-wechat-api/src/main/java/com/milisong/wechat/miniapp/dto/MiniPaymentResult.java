package com.milisong.wechat.miniapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/9/3 上午11:20
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniPaymentResult extends BaseWechat implements Serializable {
    private static final long serialVersionUID = 5625696761855970211L;

    private String code;

    private String msg;
}