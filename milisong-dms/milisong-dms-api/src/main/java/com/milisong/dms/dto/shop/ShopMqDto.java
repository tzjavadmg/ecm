package com.milisong.dms.dto.shop;

import lombok.Data;

/**
 * 接收门店消息
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
