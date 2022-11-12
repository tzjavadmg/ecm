package com.mili.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: mili-oss
 * @description: TODO
 * @author: liuyy
 * @date: 2019/5/30 20:25
 */
@Data
@ConfigurationProperties(prefix = "shopservice")
@Component
public class ShopIdProperties {
    /**
     * 1号门店ID
     */
    private Long shopIdOne;

    /**
     * 2号门店ID
     */
    private Long shopIdTwo;

}
