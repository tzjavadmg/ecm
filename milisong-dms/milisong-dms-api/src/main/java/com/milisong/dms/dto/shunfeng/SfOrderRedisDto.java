package com.milisong.dms.dto.shunfeng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaozhonghui
 * @Description 顺丰订单redis缓存数据封装
 * @date 2018-12-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SfOrderRedisDto implements Serializable {
    private static final long serialVersionUID = -6077617914172423900L;
    /** 顺丰订单id */
    private String sfOrderId;
    /** 商家订单id (集单)*/
    private String detailSetId;
    /** 已发送用户手机号 */
    private String sendUserMobile;
    /** 取单号 */
    private String description;
    /** 公司取餐点信息 */
    private List<SfOrderCompanyInfoDto> companyInfoDtos;
    /** 门店id */
    private String shopId;
}
