package com.milisong.dms.dto.shunfeng;

import lombok.Data;

import java.io.Serializable;


/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
public class SfORderProductDetail implements Serializable {
    private static final long serialVersionUID = 2300971918046068714L;
    /** 商品名称 */
    private String productName;
    /** 商品数量 */
    private Integer productNum;
}
