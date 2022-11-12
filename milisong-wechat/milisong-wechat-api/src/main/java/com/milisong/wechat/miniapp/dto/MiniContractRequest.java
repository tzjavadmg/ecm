package com.milisong.wechat.miniapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/8/31 下午3:51
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniContractRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = -1391242817280929211L;

    private String contractNo;

    private String mobileNo;

}