package com.milisong.pms.prom.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/4/22   14:16
 *    desc    :
 *    version : v1.0
 * </pre>
 */
@Configuration
@RefreshScope
@Data
public class PmsProperties {

    //调用SCM，发送IVR电话模板
    @Value("${user-point-switch:false}")
    private Boolean userPointSwitch;
}
