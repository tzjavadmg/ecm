package com.milisong.scm.shop.dto.building;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/8   10:51
 *    desc    : 公司楼层修改发送MQ的dto
 *    version : v1.0
 * </pre>
 */
@Data
public class CompanyModifyDto implements Serializable {
    private static final long serialVersionUID = -2629784212108599543L;

    //楼宇id
    private Long buildingId;

    //消息类型，1:楼层 2:公司名
    private Integer type;

    //修改前
    private String reviseFront;

    //修改后
    private String reviseAfter;

}
