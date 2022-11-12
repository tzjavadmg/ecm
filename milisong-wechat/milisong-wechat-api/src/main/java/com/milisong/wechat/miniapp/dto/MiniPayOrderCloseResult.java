package com.milisong.wechat.miniapp.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/10/31 5:05 PM
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@XStreamAlias("xml")
public class MiniPayOrderCloseResult extends BasicPayDto implements Serializable {
    private static final long serialVersionUID = 8762296660180839610L;

    /**
     * 业务结果描述
     */
    @XStreamAlias("result_msg")
    private String resultMsg;
}