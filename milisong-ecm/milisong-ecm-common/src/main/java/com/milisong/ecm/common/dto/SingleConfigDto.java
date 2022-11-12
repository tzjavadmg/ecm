package com.milisong.ecm.common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 门店信息配置(如配送费，包装费等)mq对象
 *
 */

@Getter
@Setter
public class SingleConfigDto {

    private String shopCode;

    private String configKey;

    private String configValue;

    private Byte configValueType;

}

