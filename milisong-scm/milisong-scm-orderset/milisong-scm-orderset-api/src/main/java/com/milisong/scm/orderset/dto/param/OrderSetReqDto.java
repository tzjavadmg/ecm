package com.milisong.scm.orderset.dto.param;

import com.farmland.core.api.PageParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @Description 集单查询参数
 * @date 2019-02-25
 */
@Data
public class OrderSetReqDto extends PageParam implements Serializable {

    private static final long serialVersionUID = 1124879510890680462L;
    private Long shopId;

    private String company;

    private String deliveryDate;
}
