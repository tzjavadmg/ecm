package com.milisong.upms.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户数据权限的DTO
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class UserDataPermissionDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;

    private String dataValue;

    private String dataDesc;
}
