package com.milisong.scm.base.dto.mq;

import lombok.Data;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/12   18:53
 *    desc    :   门店MQdto
 *    version : v1.0
 * </pre>
 */
@Data
public class ShopMqDto {

    private Long id;

    private String code;

    private String name;

    //门店状态:1营业中 2停业
    private Byte status;

    private String address;

    private Boolean isDelete;
}
