package com.milisong.ecm.breakfast.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description:取餐时间dto
 * @version:  1.0
 * @author:  admin
 * @createTime: 2019-02-11 11:46:08
 */
@Data
public class CompanyMealTimeDto {
    private Long id;

    /**
     * 配送时间begin
     */
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeBegin;

    /**
     * 配送时间end
     */
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeEnd;

    private String code;

    private Boolean isDeleted;

}