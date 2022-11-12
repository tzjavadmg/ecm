package com.milisong.ecm.common.user.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/17   17:03
 *    desc    : 用户模块需要的配置
 *    version : v1.0
 * </pre>
 */
@Configuration
@Data
public class UserProperties {

    @Value("${user.evaluation.show-h5-url}")
    private String evaluationShowUrl;

    @Value("${user.evaluation.short-url}")
    private String shortUrl;
}
