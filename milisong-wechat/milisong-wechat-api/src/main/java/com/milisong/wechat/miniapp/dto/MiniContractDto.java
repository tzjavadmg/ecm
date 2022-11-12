package com.milisong.wechat.miniapp.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sailor wang
 * @date 2018/9/2 下午2:28
 * @description
 */
@Data
public class MiniContractDto implements Serializable {
    private static final long serialVersionUID = -59950920971988747L;

    private String wechatContractNo;

    private Map<String,Object> extraData;//c端签约数据
}