package com.milisong.breakfast.scm.orderset.dto.param;

import com.farmland.core.api.PageParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaozhonghui
 * @Description 集单查询参数
 * @date 2019-02-25
 */
@Data
public class OrderSetReqDto extends PageParam implements Serializable {

    private Long shopId;

    private String company;

    private String deliveryDate;

    private Byte orderType;
    
    /** 配送单号 */
    private String description;
    /** 配送单号集合 */
    private List<String> descriptionList;
}
