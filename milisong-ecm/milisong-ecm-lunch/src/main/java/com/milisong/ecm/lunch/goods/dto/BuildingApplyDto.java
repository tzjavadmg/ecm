package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BuildingApplyDto {

    private Long buildingId;

    private Long userId;

    private String nikeName;

    private String mobile;

    private Date applyTime;
}