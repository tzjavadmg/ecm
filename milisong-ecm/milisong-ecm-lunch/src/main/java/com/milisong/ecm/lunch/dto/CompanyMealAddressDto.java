package com.milisong.ecm.lunch.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 早餐-公司取餐点信息
 */
@Getter
@Setter
public class CompanyMealAddressDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2381470216080552377L;

	/**
     * 
     */
    private Long id;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 取餐点名称
     */
    private String mealAddress;

    /**
     * 取餐点图片
     */
    private String picture;
    
    
    /**
     * 取餐点图片(绝对路径)
     */
    private String completePicture;
 
}