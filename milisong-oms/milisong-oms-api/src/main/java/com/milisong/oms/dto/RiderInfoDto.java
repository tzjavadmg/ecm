package com.milisong.oms.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RiderInfoDto {

    //取餐号
    private String takeNo;

    //配送员名称
    private String transporterName;

    //配送员电话
    private String transporterPhone;

    //顺风状态信息
    private List<ShungFengStatusDto> list = new ArrayList();

}
