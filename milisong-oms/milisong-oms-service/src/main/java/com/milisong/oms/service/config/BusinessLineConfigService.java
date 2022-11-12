package com.milisong.oms.service.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @ Description：配置项服务
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/4 11:33
 */
public interface BusinessLineConfigService {

    BusinessLineConfig getConfig();

    BusinessLineConfig getConfig(Byte businessLine);

    @Getter
    @Setter
    static class BusinessLineConfig {

        private Integer unPayExpiredTime;

    }

}
