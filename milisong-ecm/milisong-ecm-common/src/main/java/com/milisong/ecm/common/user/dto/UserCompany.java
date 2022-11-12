package com.milisong.ecm.common.user.dto;
import java.io.Serializable;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCompany implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String name;

    private String abbreviation;

    private Long buildingId;

    private String buildingAbbreviation;

    private String floor;

    private String roomNumber;

    private Integer employeesNumber;

    private Boolean isCertification;

    private Boolean isSameName;

    private String sameName;

    private Boolean isDeleted;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}
