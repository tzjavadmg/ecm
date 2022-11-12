package com.milisong.dms.dto.shop;

import com.milisong.dms.dto.shunfeng.SfOrderCompanyInfoDto;
import lombok.Data;

import java.util.List;

/**
 * @author zhaozhonghui
 * @Description 地址信息
 * @date 2018-12-19
 */
@Data
public class AddressDetailInfo {
    /** 纬度 */
    private String userLat;
    /** 经度 */
    private String userLng;
    /** 公司取餐点信息 */
    private List<SfOrderCompanyInfoDto> companyInfoDtos;
}
