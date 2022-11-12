package com.milisong.scm.shop.dto.building;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * 下拉的公司信息dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CompanyChoiceDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5928513023064441992L;

	private Long id;

    private String abbreviation;

    private Boolean isCertification;

    private Boolean isSameName;
}