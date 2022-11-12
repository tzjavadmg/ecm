package com.milisong.oms.dto.shunfeng;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
@Component
@ConfigurationProperties(prefix = "sf")
public class SfOrderBaseDto {

    private String devId;

    private String key;

    private int version;

    private String baseUrl;
}
