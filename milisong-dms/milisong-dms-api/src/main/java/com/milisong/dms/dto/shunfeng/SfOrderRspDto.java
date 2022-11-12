package com.milisong.dms.dto.shunfeng;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaozhonghui
 * @Description 顺丰订单数据封装
 * @date 2018-12-04
 */
@Data
public class SfOrderRspDto implements Serializable {
    private static final long serialVersionUID = -4310783930615882588L;

    private Long id;

    private String sfOrderId;

    private String detailSetId;

    private Date distributeTime;

    private Byte businessType;
}
