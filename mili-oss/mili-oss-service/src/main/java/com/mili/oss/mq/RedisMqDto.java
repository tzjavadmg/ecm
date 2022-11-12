package com.mili.oss.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @Description http请求redis信息参数封装
 * @date 2019-03-11
 */
@Data
public class RedisMqDto implements Serializable {

    private Boolean hash;

    private String hkey;

    private String key;
}
