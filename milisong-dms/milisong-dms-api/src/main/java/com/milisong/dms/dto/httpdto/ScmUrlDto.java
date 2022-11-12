package com.milisong.dms.dto.httpdto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author zhaozhonghui
 * @Description http接口请求路径封装
 * @date 2018-12-17
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm")
public class ScmUrlDto {

    private String queryLunchByOrderNoUrl;

    private String queryLunchBySetIdUrl;

    private String queryBfByOrderNoUrl;

    private String queryBfBySetIdUrl;

    private String queryAllShopUrl;

    private String queryShopByIdUrl;

    private String queryBuildingUrl;

    private String queryCompanyUrl;
}
