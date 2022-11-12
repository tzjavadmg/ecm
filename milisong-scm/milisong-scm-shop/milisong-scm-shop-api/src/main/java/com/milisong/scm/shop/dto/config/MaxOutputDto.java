package com.milisong.scm.shop.dto.config;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   10:56
 *    desc    : 门店最大产量dto
 *    version : v1.0
 * </pre>
 */
@Data
public class MaxOutputDto implements Serializable{

    private Long id;

    private Long shopId;

    private String shopCode;

    private String shopName;

    private Integer maxOutput;

    private String createBy;

    private String updateBy;
}
