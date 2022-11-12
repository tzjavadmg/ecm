package com.milisong.dms.dto.shunfeng;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @Description 顺丰订单 公司早餐取餐点信息封装
 * @date 2018-12-06
 */
@Data
public class SfOrderCompanyInfoDto implements Serializable {

    private static final long serialVersionUID = 6788268858300406468L;

    private String companyName;

    private Long companyId;

    private String companyMealAddress;

    private String mealAddressPicture;
}
