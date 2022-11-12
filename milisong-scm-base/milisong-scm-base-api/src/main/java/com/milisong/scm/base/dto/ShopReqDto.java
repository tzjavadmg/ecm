package com.milisong.scm.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/7   11:38
 *    desc    : 门店信息请求dto
 *    version : v1.0
 * </pre>
 */
@Data
public class ShopReqDto implements Serializable {
    private static final long serialVersionUID = 8105088044613414199L;

    private Long id;

    private String code;

    private String name;

    private Byte status;

    private String address;

    private Boolean isDelete;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private Long srcId;

}
