package com.milisong.scm.base.propreties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述:oss文件上传，配置属性类
 * 创建者:田海波 于 2018年09月04日 14:39.
 */
@Data
@ConfigurationProperties(prefix = "file.oss")
@Component
public class OssFileProperties {

    private String localBackupPath;

    private String uploadUrl;

    private String viewUrl;

    private String maxSingleFileSize;

    private String[]  format;
    
    private Byte bizType;
}
